package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringAutoService;

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
 * SpringAutoServiceProcessor
 * </p>
 *
 * @author livk
 */
@AutoService(Processor.class)
public class SpringAutoServiceProcessor extends CustomizeAbstractProcessor {

    private static final Class<SpringAutoService> SUPPORT_CLASS = SpringAutoService.class;

    private static final String AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.AutoConfiguration";

    private static final String LOCATION = "META-INF/spring/%s.imports";

    private final Map<String, Set<String>> importsMap = new ConcurrentHashMap<>();

    @Override
    protected Set<Class<?>> getSupportedAnnotation() {
        return Set.of(SUPPORT_CLASS);
    }

    @Override
    protected void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();
        for (String providerInterface : importsMap.keySet()) {
            String resourceFile = String.format(LOCATION, providerInterface);
            try {
                Set<String> exitImports = new HashSet<>();
                try {
                    for (StandardLocation standardLocation : StandardLocation.values()) {
                        if (!standardLocation.isOutputLocation() && standardLocation.name().endsWith("_PATH")) {
                            FileObject resource = filer.getResource(standardLocation, "", resourceFile);
                            exitImports.addAll(SpringImportsUtils.read(resource));
                        }
                    }
                } catch (IOException ignored) {

                }
                Set<String> allImports = Stream.concat(exitImports.stream(), importsMap.get(providerInterface).stream())
                        .collect(Collectors.toSet());
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);

                try (OutputStream out = fileObject.openOutputStream()) {
                    SpringImportsUtils.writeFile(allImports, out);
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
                Optional<String> optionalProvider = Optional.ofNullable(elementValues.get("auto"))
                        .map(annotationValue -> annotationValue.getValue().toString());
                String provider = optionalProvider.orElse(AUTOCONFIGURATION);
                Set<String> providers = importsMap.computeIfAbsent(provider, k -> new HashSet<>());
                providers.add(element.toString());
            }
        }
    }
}
