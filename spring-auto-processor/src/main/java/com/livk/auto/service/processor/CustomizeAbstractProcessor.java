package com.livk.auto.service.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.StandardLocation;
import java.util.Map;
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

    /**
     * The Filer.
     */
    protected Filer filer;

    /**
     * The Elements.
     */
    protected Elements elements;

    /**
     * The Messager.
     */
    protected Messager messager;

    /**
     * The Options.
     */
    protected Map<String, String> options;

    /**
     * The Types.
     */
    protected Types types;

    /**
     * The Out.
     */
    protected Set<StandardLocation> out;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        out = Set.of(StandardLocation.CLASS_OUTPUT, StandardLocation.SOURCE_OUTPUT);
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        options = processingEnv.getOptions();
        types = processingEnv.getTypeUtils();
    }

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
     * @param <T>         annotation
     * @param element     element
     * @param targetClass annotation class
     * @return AnnotationMirror annotation mirror with
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
