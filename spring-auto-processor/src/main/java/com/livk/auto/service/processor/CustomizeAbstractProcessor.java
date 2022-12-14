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

    /**
     * Set supported annotations
     *
     * @return Set class
     */
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

    /**
     * 生成文件
     */
    protected abstract void generateConfigFiles();

    /**
     * 处理注解
     *
     * @param annotations annotations
     * @param roundEnv    roundEnv
     */
    protected abstract void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    /**
     * Get Mirror annotation meta properties
     *
     * @param element     element
     * @param targetClass annotation class
     * @param <T>         annotation
     * @return AnnotationMirror
     */
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
