package com.fightyz.lifecycle.processor;

import com.fightyz.lifecycle.annotation.OnActivityCreated;
import com.fightyz.lifecycle.annotation.OnActivityDestroy;
import com.fightyz.lifecycle.annotation.OnProductConnected;
import com.fightyz.lifecycle.annotation.OnProductDisconnected;
import com.fightyz.lifecycle.meta.SimpleSubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfo;
import com.fightyz.lifecycle.meta.SubscriberInfoIndex;
import com.fightyz.lifecycle.meta.SubscriberMethodInfo;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import de.greenrobot.common.ListMap;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {"com.fightyz.lifecycle.annotation.OnActivityCreated",
        "com.fightyz.lifecycle.annotation.OnActivityDestroy",
        "com.fightyz.lifecycle.annotation.OnProductConnected",
        "com.fightyz.lifecycle.annotation.OnProductDisconnected"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(value = {"lifecycleIndex"})
public class LifecycleAnnotationProcessor extends AbstractProcessor {

    public static final String OPTION_LIFECYCLE_INDEX = "lifecycleIndex";

    private static final String PRODUCT_SUBSCRIBER_INDEX_FIELD_NAME = "PRODUCT_SUBSCRIBER_INDEX";
    private static final String ACTIVITY_SUBSCRIBER_INDEX_FIELD_NAME = "ACTIVITY_SUBSCRIBER_INDEX";

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    private final ListMap<TypeElement, ExecutableElement> productMethodsByClass = new ListMap<>();
    private final ListMap<TypeElement, ExecutableElement> activityMethodsByClass = new ListMap<>();
    private Set<TypeElement> typeElements;
    private int round;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        typeElements = ProcessingUtils.getTypeElementsToProcess(
                roundEnvironment.getRootElements(), annotations);

        for (TypeElement typeElement : typeElements) {
            String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            String typeName = typeElement.getSimpleName().toString();
            PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("package name %s", packageName));
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("class name %s", typeName));
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("package simple name %s", packageElement.getSimpleName()));
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("package enclosing element %s", packageElement.getEnclosingElement()));
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("package enclosed element %s", packageElement.getEnclosedElements()));
        }

        String index = processingEnv.getOptions().get(OPTION_LIFECYCLE_INDEX);
        if (index == null) {
            messager.printMessage(Diagnostic.Kind.NOTE, "No option " + OPTION_LIFECYCLE_INDEX +
                    " passed to annotation processor");
            return false;
        }

        int period = index.lastIndexOf('.');
        String indexPackage = period != -1 ? index.substring(0, period) : null;
        String clazz = index.substring(period + 1);
        round++;
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing round " + round + ", new annotations " +
                !annotations.isEmpty() + ", processingOver: " + roundEnvironment.processingOver());
        if (roundEnvironment.processingOver()) {
            if (!annotations.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "Unexpected processing state: annotations still available after processing over");
                return false;
            }
        }
        if (annotations.isEmpty()) {
            return false;
        }
        collectSubscribers(annotations, roundEnvironment, messager);
        generateIndexFile(indexPackage, clazz);
        return true;
    }

    private void generateIndexFile(String packageName, String indexClassName) {
        ClassName className = ClassName.get(packageName, indexClassName);

        ClassName simpleSubscriberInfoClass = ClassName.get(SimpleSubscriberInfo.class);

        TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
        ClassName klass = ClassName.get(Class.class);
        TypeName classOfAny = ParameterizedTypeName.get(klass, wildcard);

        TypeName setOfAnyClass = ParameterizedTypeName.get(ClassName.get(Set.class), classOfAny);

        TypeSpec.Builder indexClassBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(SubscriberInfoIndex.class);

        FieldSpec productSubscriberIndex = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(Map.class), classOfAny,
                        ClassName.get(SubscriberInfo.class)), PRODUCT_SUBSCRIBER_INDEX_FIELD_NAME,
                Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T()", HashMap.class)
                .build();

        FieldSpec activitySubscriberIndex = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(Map.class), classOfAny,
                        ClassName.get(SubscriberInfo.class)), ACTIVITY_SUBSCRIBER_INDEX_FIELD_NAME,
                Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T()", HashMap.class)
                .build();

        MethodSpec putProductIndexMethod = generatePutProductIndexMethod(productSubscriberIndex);

        MethodSpec putActivityIndexMethod = generatePutActivityIndexMethod(activitySubscriberIndex);

        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();
        generateProductIndexBlock(simpleSubscriberInfoClass, putProductIndexMethod, staticBlockBuilder);
        generateActivityIndexBlock(simpleSubscriberInfoClass, putActivityIndexMethod, staticBlockBuilder);
        CodeBlock staticBlock = staticBlockBuilder.build();
        messager.printMessage(Diagnostic.Kind.NOTE, "staticBlock " + staticBlock);

        MethodSpec getSubscriberInfoMethod = generateGetSubscriberInfoMethod(classOfAny, productSubscriberIndex, activitySubscriberIndex);

        MethodSpec getActivitySubscriberClassesMethod = generateGetActivitySubscriberClasses(setOfAnyClass, activitySubscriberIndex);
        MethodSpec getProductSubscriberClassesMethod = generateGetProductSubscriberClasses(setOfAnyClass, activitySubscriberIndex);

        TypeSpec classSpec = indexClassBuilder.addField(productSubscriberIndex)
                .addField(activitySubscriberIndex)
                .addStaticBlock(staticBlock)
                .addMethod(getSubscriberInfoMethod)
                .addMethod(getActivitySubscriberClassesMethod)
                .addMethod(getProductSubscriberClassesMethod)
                .addMethod(putProductIndexMethod)
                .addMethod(putActivityIndexMethod)
                .build();


        JavaFile javaFile = JavaFile.builder(packageName, classSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException("Could not write source for " + indexClassName, e);
        }

    }

    private MethodSpec generateGetActivitySubscriberClasses(TypeName setOfAnyClass, FieldSpec activitySubscriberIndex) {
        MethodSpec getActivitySubscriberClassesMethod = MethodSpec.methodBuilder("getActivitySubscriberClasses")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(setOfAnyClass)
                .addStatement("return $N.keySet()", activitySubscriberIndex)
                .build();
        messager.printMessage(Diagnostic.Kind.NOTE, "generateGetActivitySubscriberClasses " + getActivitySubscriberClassesMethod);
        return getActivitySubscriberClassesMethod;
    }

    private MethodSpec generateGetProductSubscriberClasses(TypeName setOfAnyClass, FieldSpec productSubscriberIndex) {
        MethodSpec getProductSubscriberClassesMethod = MethodSpec.methodBuilder("getProductSubscriberClasses")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(setOfAnyClass)
                .addStatement("return $N.keySet()", productSubscriberIndex)
                .build();
        messager.printMessage(Diagnostic.Kind.NOTE, "generateGetProductSubscriberClasses " + getProductSubscriberClassesMethod);
        return getProductSubscriberClassesMethod;
    }

    private MethodSpec generatePutActivityIndexMethod(FieldSpec activitySubscriberIndex) {
        MethodSpec putActivityIndexMethod = MethodSpec.methodBuilder("putActivityIndex")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(SubscriberInfo.class, "info")
                .addStatement("$N.put(info.getSubscriberClass(), info)", activitySubscriberIndex)
                .build();
        messager.printMessage(Diagnostic.Kind.NOTE, "generatePutActivityIndexMethod " +
                putActivityIndexMethod);
        return putActivityIndexMethod;
    }

    private MethodSpec generatePutProductIndexMethod(FieldSpec productSubscriberIndex) {
        MethodSpec putProductIndexMethod = MethodSpec.methodBuilder("putProductIndex")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(SubscriberInfo.class, "info")
                .addStatement("$N.put(info.getSubscriberClass(), info)", productSubscriberIndex)
                .build();
        messager.printMessage(Diagnostic.Kind.NOTE, "generatePutProductIndexMethod " +
                putProductIndexMethod);
        return putProductIndexMethod;

    }

    private MethodSpec generateGetSubscriberInfoMethod(TypeName classOfAny, FieldSpec productSubscriberIndex, FieldSpec activitySubscriberIndex) {
        MethodSpec getSubscriberInfoMethod = MethodSpec.methodBuilder("getSubscriberInfo")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(classOfAny, "subscriberClass").build())
                .returns(SubscriberInfo.class)
                .addCode(CodeBlock.builder()
                        .addStatement("$T productInfo = $N.get(subscriberClass)",
                                SubscriberInfo.class, productSubscriberIndex)
                        .addStatement("$T activityInfo = $N.get(subscriberClass)",
                                SubscriberInfo.class, activitySubscriberIndex)
                        .beginControlFlow("if (productInfo != null && activityInfo != null)")
                        .addStatement("return $T.combine(productInfo, activityInfo)", SimpleSubscriberInfo.class)
                        .nextControlFlow("else if (activityInfo != null)")
                        .addStatement("return activityInfo")
                        .nextControlFlow("else")
                        .addStatement("return productInfo")
                        .endControlFlow()
                        .build())
                .build();
        messager.printMessage(Diagnostic.Kind.NOTE, "generateGetSubscriberInfoMethod " + getSubscriberInfoMethod);
        return getSubscriberInfoMethod;
    }

    private void generateActivityIndexBlock(ClassName simpleSubscriberInfoClass, MethodSpec putActivityIndexMethod, CodeBlock.Builder staticBlockBuilder) {
        List<String> subscriberMethodInfoBlockList = new LinkedList<>();
        for (TypeElement activityClass : activityMethodsByClass.keySet()) {
            List<ExecutableElement> methods = activityMethodsByClass.get(activityClass);
            for (ExecutableElement method : methods) {
                OnActivityCreated onActivityCreated = method.getAnnotation(OnActivityCreated.class);
                OnActivityDestroy onActivityDestroy = method.getAnnotation(OnActivityDestroy.class);
                if (onActivityCreated != null) {
                    String segment = CodeBlock.builder()
                            .add("new $T($S, $T.class)", SubscriberMethodInfo.class,
                                    method.getSimpleName(), OnActivityCreated.class)
                            .build().toString();
                    subscriberMethodInfoBlockList.add(segment);
                } else if (onActivityDestroy != null) {
                    String segment = CodeBlock.builder()
                            .add("new $T($S, $T.class)", SubscriberMethodInfo.class,
                                    method.getSimpleName(), OnActivityDestroy.class)
                            .build().toString();
                    subscriberMethodInfoBlockList.add(segment);
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Wrong annotation. class " + activityClass +
                            ", method " + method + ", annotation " + method.getAnnotationMirrors());
                }
            }
            String subscriberMethodInfoElements = String.join(",", subscriberMethodInfoBlockList);

            String subscriberMethodInfoArrayBlock = CodeBlock.builder()
                    .add("new $T[]{$L}", SubscriberMethodInfo.class, subscriberMethodInfoElements)
                    .build().toString();
            staticBlockBuilder.addStatement("$N(new $T($T.class, $L))", putActivityIndexMethod,
                    simpleSubscriberInfoClass, ClassName.get(activityClass), subscriberMethodInfoArrayBlock);
        }
    }

    private void generateProductIndexBlock(ClassName simpleSubscriberInfoClass, MethodSpec putProductIndexMethod, CodeBlock.Builder staticBlockBuilder) {
        List<String> subscriberMethodInfoBlockList = new LinkedList<>();
        for (TypeElement productClass : productMethodsByClass.keySet()) {
            List<ExecutableElement> methods = productMethodsByClass.get(productClass);
            for (ExecutableElement method : methods) {
                OnProductConnected onProductConnectedAnnotation = method.getAnnotation(OnProductConnected.class);
                OnProductDisconnected onProductDisconnectedAnnotation = method.getAnnotation(OnProductDisconnected.class);
                if (onProductConnectedAnnotation != null) {
                    String segment = CodeBlock.builder()
                            .add("new $T($S, $T.class)", SubscriberMethodInfo.class,
                                    method.getSimpleName(), OnProductConnected.class)
                            .build().toString();
                    subscriberMethodInfoBlockList.add(segment);
                } else if (onProductDisconnectedAnnotation != null) {
                    String segment = CodeBlock.builder()
                            .add("new $T($S, $T.class)", SubscriberMethodInfo.class,
                                    method.getSimpleName(), OnProductDisconnected.class)
                            .build().toString();
                    subscriberMethodInfoBlockList.add(segment);
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Wrong annotation. class " + productClass +
                            ", method " + method + ", annotation " + method.getAnnotationMirrors());
                }
            }
            String subscriberMethodInfoElements = String.join(",", subscriberMethodInfoBlockList);
            messager.printMessage(Diagnostic.Kind.NOTE, "subscriberMethodInfoElements " + subscriberMethodInfoElements);
            TypeSpec subscriberMethodInfoBlock = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ArrayTypeName.get(SubscriberMethodInfo.class))
                    .addInitializerBlock(CodeBlock.builder()
                            .addStatement(subscriberMethodInfoElements).build())
                    .build();

            String subscriberMethodInfoArrayBlock = CodeBlock.builder()
                    .add("new $T[]{$L}", SubscriberMethodInfo.class, subscriberMethodInfoElements)
                    .build().toString();

            messager.printMessage(Diagnostic.Kind.NOTE, "check null putProductIndexMethod " + putProductIndexMethod + "\n" +
                    "simpleSubscriberInfoClass " + simpleSubscriberInfoClass + "\n" +
                    "productClass " + productClass + "\n" +
                    "subscriberMethodInfoBlock " + subscriberMethodInfoBlock);
            staticBlockBuilder.addStatement("$N(new $T($T.class, $L))", putProductIndexMethod,
                    simpleSubscriberInfoClass, ClassName.get(productClass), subscriberMethodInfoArrayBlock);
        }
    }

    private void collectSubscribers(Set<? extends TypeElement> annotations, RoundEnvironment env, Messager messager) {
        for (TypeElement annotation : annotations) {

            Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element instanceof ExecutableElement) {
                    ExecutableElement method = (ExecutableElement) element;
                    if (checkHasNoErrors(method, messager)) {
                        TypeElement classElement = (TypeElement) method.getEnclosingElement();
                        OnProductConnected onProductConnected = method.getAnnotation(OnProductConnected.class);
                        OnProductDisconnected onProductDisconnected = method.getAnnotation(OnProductDisconnected.class);
                        OnActivityCreated onActivityCreated = method.getAnnotation(OnActivityCreated.class);
                        OnActivityDestroy onActivityDestroy = method.getAnnotation(OnActivityDestroy.class);
                        if (onProductConnected != null || onProductDisconnected != null) {
                            productMethodsByClass.putElement(classElement, method);
                        }
                        if (onActivityCreated != null || onActivityDestroy != null) {
                            activityMethodsByClass.putElement(classElement, method);
                        }
                    }
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Annotation " + annotation +
                            " is only valid for methods", element);
                }
            }
        }
    }

    private boolean checkHasNoErrors(ExecutableElement element, Messager messager) {
        if (element.getModifiers().contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must not be static", element);
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must be public", element);
            return false;
        }

        List<? extends VariableElement> parameters = ((ExecutableElement) element).getParameters();
        if (parameters.size() != 0) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Subscriber method must have exactly 0 parameter", element);
            return false;
        }
        return true;
    }
}
