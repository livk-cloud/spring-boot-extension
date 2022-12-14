package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringFactories;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * SpringFactoriesProcessor
 * </p>
 *
 * @author livk
 */
@AutoService(Processor.class)
public class SpringFactoriesProcessor extends CustomizeAbstractProcessor {

    private static final Class<SpringFactories> SUPPORT_CLASS = SpringFactories.class;

    private static final String LOCATION = "META-INF/spring.factories";

    private final Map<String, Set<String>> providerMap = new ConcurrentHashMap<>();

    @Override
    protected Set<Class<?>> getSupportedAnnotation() {
        return Set.of(SUPPORT_CLASS);
    }

    @Override
    protected void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();
        for (String providerInterface : providerMap.keySet()) {
            try {
                Set<String> exitImports = new HashSet<>();
                try {
                    for (StandardLocation standardLocation : StandardLocation.values()) {
                        if (!standardLocation.isOutputLocation()) {
                            FileObject resource = filer.getResource(standardLocation, "", LOCATION);
                            exitImports.addAll(SpringFactoriesUtils.read(providerInterface, resource));
                        }
                    }
                } catch (IOException ignored) {

                }
                Set<String> allImports = Stream.concat(exitImports.stream(), providerMap.get(providerInterface).stream())
                        .collect(Collectors.toSet());
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", LOCATION);

                try (OutputStream out = fileObject.openOutputStream()) {
                    SpringFactoriesUtils.writeFile(providerInterface, allImports, out);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
        for (Element element : elements) {
            AnnotationMirror annotationMirror = getAnnotationMirrorWith(element, SUPPORT_CLASS);
            if (annotationMirror != null) {
                Map<String, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue));
                Optional<String> optionalProvider = Optional.ofNullable(elementValues.get("type"))
                        .map(annotationValue -> annotationValue.getValue().toString());
                String provider = optionalProvider.orElseThrow();
                Set<String> providers = providerMap.computeIfAbsent(provider, k -> new HashSet<>());
                providers.add(element.toString());
            }
        }
    }
}
