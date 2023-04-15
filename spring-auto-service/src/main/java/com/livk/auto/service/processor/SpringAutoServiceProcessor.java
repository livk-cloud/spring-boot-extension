package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringAutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
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
        for (String providerInterface : importsMap.keySet()) {
            String resourceFile = String.format(LOCATION, providerInterface);
            try {
                FileObject resource = filer.getResource(out, "", resourceFile);
                Set<String> exitImports = this.read(resource);
                Set<String> allImports = Stream.concat(exitImports.stream(), importsMap.get(providerInterface).stream())
                        .collect(Collectors.toSet());
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);

                this.writeFile(allImports, fileObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
        for (Element element : elements) {
            Optional<String> value = super.getAnnotationMirrorAttributes(element, SUPPORT_CLASS, "value");
            String provider = super.transform(value.orElse(AUTOCONFIGURATION));
            String serviceImpl = super.transform(element.toString());
            super.factoriesAdd(importsMap, provider, serviceImpl);
        }
    }

    /**
     * 从文件读取配置
     *
     * @param fileObject 文件信息
     * @return set className
     */
    private Set<String> read(FileObject fileObject) {
        try (BufferedReader reader = bufferedReader(fileObject)) {
            return reader.lines()
                    .map(String::trim)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (IOException ignored) {
            return Collections.emptySet();
        }
    }

    /**
     * 将配置信息写入到文件
     *
     * @param services   实现类信息
     * @param fileObject 文件信息
     */
    private void writeFile(Collection<String> services, FileObject fileObject) {
        try (BufferedWriter writer = bufferedWriter(fileObject)) {
            for (String service : services) {
                writer.write(service);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ignored) {

        }
    }
}
