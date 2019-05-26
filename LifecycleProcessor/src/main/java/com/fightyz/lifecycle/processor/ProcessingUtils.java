package com.fightyz.lifecycle.processor;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Copyright ©2019 DJI All Rights Reserved.
 *
 * @Author: joe.yang@dji.com
 * @Data: 1/2/19 5:18 PM
 * Description: ${TODO}
 */
public class ProcessingUtils {

    private ProcessingUtils(){}

    public static Set<TypeElement> getTypeElementsToProcess(Set<? extends Element> elements,
                                                            Set<? extends Element> supportedAnnotations) {

        Set<TypeElement> typeElements = new HashSet<>();
        for (Element element : elements) {
            if (element instanceof TypeElement) {
                boolean found = false;
                for (Element subElement : element.getEnclosedElements()) {
                    for (AnnotationMirror mirror : subElement.getAnnotationMirrors()) {
                        for (Element annotation : supportedAnnotations) {
                            if (mirror.getAnnotationType().asElement().equals(annotation)) {
                                typeElements.add((TypeElement) element);
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
        }
        return typeElements;
    }
}
