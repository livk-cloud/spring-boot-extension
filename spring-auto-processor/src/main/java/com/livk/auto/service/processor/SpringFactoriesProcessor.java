package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringFactories;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
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

    private static final String SPRING_LOCATION = "META-INF/spring.factories";

    private static final String AOT_LOCATION = "META-INF/spring/aot.factories";

    private final Map<String, Set<String>> springFactoriesMap = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> aotFactoriesMap = new ConcurrentHashMap<>();

    @Override
    protected Set<Class<?>> getSupportedAnnotation() {
        return Set.of(SUPPORT_CLASS);
    }

    @Override
    protected void generateConfigFiles() {
        generateConfigFiles(springFactoriesMap, SPRING_LOCATION);
        generateConfigFiles(aotFactoriesMap, AOT_LOCATION);
    }

    private void generateConfigFiles(Map<String, Set<String>> factoriesMap, String location) {
        if (!factoriesMap.isEmpty()) {
            Map<String, Set<String>> allImportMap = new HashMap<>();
            for (String providerInterface : factoriesMap.keySet()) {
                Set<String> exitImports = new HashSet<>();
                try {
                    for (StandardLocation standardLocation : out) {
                        FileObject resource = filer.getResource(standardLocation, "", location);
                        exitImports.addAll(SpringFactoriesUtils.read(providerInterface, resource));
                    }
                } catch (IOException ignored) {

                }
                Set<String> allImports = Stream.concat(exitImports.stream(), factoriesMap.get(providerInterface).stream())
                        .collect(Collectors.toSet());
                allImportMap.put(providerInterface, allImports);
            }
            try {
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", location);
                try (OutputStream out = fileObject.openOutputStream()) {
                    SpringFactoriesUtils.writeFile(allImportMap, out);
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
                Map<String, String> attributes = getAnnotationMirrorAttributes(annotationMirror);
                Optional<String> optionalProvider = Optional.ofNullable(attributes.get("type"));
                Optional<Boolean> optionalAot = Optional.ofNullable(attributes.get("aot"))
                        .map(Boolean::parseBoolean);
                String provider = optionalProvider.orElseThrow();
                boolean aot = optionalAot.orElse(false);
                if (aot) {
                    factoriesAdd(aotFactoriesMap, provider, element.toString());
                } else {
                    factoriesAdd(springFactoriesMap, provider, element.toString());
                }
            }
        }
    }
}
