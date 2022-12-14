package com.livk.auto.service.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * CustomizeAbstractProcessor
 * </p>
 *
 * @author livk
 * @date 2022/12/14
 */
public abstract class CustomizeAbstractProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return getSupportedAnnotation().stream()
                .map(Class::getName)
                .collect(Collectors.toSet());
    }

    protected abstract Set<Class<?>> getSupportedAnnotation();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(annotations, roundEnv);
        }
        return false;
    }

    protected abstract void generateConfigFiles();

    protected abstract void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected <T> AnnotationMirror getAnnotationMirrorWith(Element element, Class<T> targetClass) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeElement typeElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
            if (typeElement.getQualifiedName().contentEquals(targetClass.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }
}
