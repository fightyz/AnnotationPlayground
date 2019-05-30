package com.fightyz.lifecycle.processor;

import com.fightyz.lifecycle.annotation.OnProductConnected;
import com.fightyz.lifecycle.annotation.OnProductDisconnected;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class LifecycleAnnotationProcessor extends AbstractProcessor {

    private static final String PACKAGE_NAME = "com.fightyz.lifecycle";
    private static final String OBSERVER_HOLDER_SIMPLE_NAME = "ObserverHolder";
    private static final String ABSTRACT_OBSERVER_SIMPLE_NAME = "AbstractObserver";

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    private Set<TypeElement> typeElements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver() && !annotations.isEmpty()) {
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

            generateObserverHolder();

            generateAbstractObserver();
        }
        return true;
    }

    private void generateAbstractObserver() {
        ClassName abstractObserver = ClassName.get(PACKAGE_NAME, ABSTRACT_OBSERVER_SIMPLE_NAME);
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(abstractObserver)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

        ClassName observerHolder = ClassName.get(PACKAGE_NAME, OBSERVER_HOLDER_SIMPLE_NAME);
        FieldSpec triplets = FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Class.class), observerHolder),
                "triplets", Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T()", HashMap.class)
                .build();

        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();
        for (TypeElement typeElement : typeElements) {
            staticBlockBuilder.addStatement("$N.put($T.class, new " + OBSERVER_HOLDER_SIMPLE_NAME + "($T.class))",
                    triplets, typeElement, typeElement);
        }
        CodeBlock staticBlock = staticBlockBuilder.build();

        FieldSpec observerClasses = FieldSpec.builder(ParameterizedTypeName.get(Set.class, Class.class),
                "observerClasses", Modifier.PRIVATE)
                .initializer("new $T()", HashSet.class)
                .build();

        MethodSpec constructor = MethodSpec.constructorBuilder().varargs().addParameter(Class[].class, "classes")
                .beginControlFlow("for (Class clazz : classes)")
                .addStatement("$N.add(clazz)", observerClasses)
                .endControlFlow()
                .build();

        classBuilder
                .addField(triplets)
                .addField(observerClasses)
                .addStaticBlock(staticBlock)
                .addMethod(constructor);
        try {
            JavaFile.builder(PACKAGE_NAME,
                    classBuilder.build())
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private void generateObserverHolder() {
        ClassName observerHolder = ClassName.get(PACKAGE_NAME, OBSERVER_HOLDER_SIMPLE_NAME);
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(observerHolder)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec clazz = FieldSpec.builder(Class.class, "clazz").build();
        FieldSpec instance = FieldSpec.builder(Object.class, "instance").build();
        FieldSpec counter = FieldSpec.builder(int.class, "counter").build();

        MethodSpec constructor = MethodSpec.constructorBuilder().addParameter(Class.class, "clazz")
                .addStatement("this.$N = clazz", clazz)
                .build();

        MethodSpec equals = MethodSpec.methodBuilder("equals")
                .addAnnotation(Override.class)
                .returns(boolean.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "o")
                .addCode(equalsCode(observerHolder, "o", instance))
                .build();

        MethodSpec hashCode = MethodSpec.methodBuilder("hashCode")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(int.class)
                .addStatement("return Objects.hash($N)", instance)
                .build();

        classBuilder.addField(clazz)
                .addField(instance)
                .addField(counter)
                .addMethod(constructor)
                .addMethod(equals)
                .addMethod(hashCode);
        try {
            JavaFile.builder(PACKAGE_NAME,
                    classBuilder.build())
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private CodeBlock equalsCode(ClassName className, String parameter, FieldSpec field) {
        return CodeBlock.builder()
                .beginControlFlow("if (this == $N)", parameter)
                .addStatement("return true")
                .endControlFlow()
                .beginControlFlow("if (!($N instanceof ObserverHolder))", parameter)
                .addStatement("return false")
                .endControlFlow()
                .addStatement("$N that = ($N) $N", className.simpleName(), className.simpleName(), parameter)
                .addStatement("return $T.equals($N, that.$N)", Objects.class, field, field)
                .build();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                OnProductConnected.class.getCanonicalName(),
                OnProductDisconnected.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
