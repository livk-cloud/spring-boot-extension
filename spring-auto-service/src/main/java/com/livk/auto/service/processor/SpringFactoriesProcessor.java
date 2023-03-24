package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringFactories;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Charsets.UTF_8;

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
            try {
                for (String providerInterface : factoriesMap.keySet()) {
                    Set<String> exitImports = new HashSet<>();
                    for (StandardLocation standardLocation : out) {
                        FileObject resource = filer.getResource(standardLocation, "", location);
                        exitImports.addAll(this.read(providerInterface, resource));
                    }
                    Set<String> allImports = Stream.concat(exitImports.stream(), factoriesMap.get(providerInterface).stream())
                            .collect(Collectors.toSet());
                    allImportMap.put(providerInterface, allImports);
                }
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", location);
                try (OutputStream out = fileObject.openOutputStream()) {
                    this.writeFile(allImportMap, out);
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
            Optional<String> value = super.getAnnotationMirrorAttributes(element, SUPPORT_CLASS, "value");
            String provider = super.transform(value.orElseThrow());
            boolean aot = element.getAnnotation(SUPPORT_CLASS).aot();
            String serviceImpl = super.transform(element.toString());
            if (aot) {
                super.factoriesAdd(aotFactoriesMap, provider, serviceImpl);
            } else {
                super.factoriesAdd(springFactoriesMap, provider, serviceImpl);
            }
        }
    }

    /**
     * 从文件读取某个接口的配置
     *
     * @param providerInterface 供应商接口
     * @param fileObject        文件信息
     * @return set className
     */
    private Set<String> read(String providerInterface, FileObject fileObject) {
        try (BufferedReader reader = new BufferedReader(fileObject.openReader(true))) {
            boolean read = false;
            List<String> lines = reader.lines().toList();
            Set<String> providers = new HashSet<>();
            for (String line : lines) {
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }
                if (line.equals(providerInterface + "=\\")) {
                    read = true;
                    continue;
                }
                if (line.endsWith("=\\")) {
                    read = false;
                    continue;
                }
                if (read) {
                    providers.add(line.replaceAll(",\\\\", "").trim());
                }
            }
            return providers;
        } catch (IOException e) {
            return Set.of();
        }
    }

    /**
     * 将配置信息写入到文件
     *
     * @param allImportMap 供应商接口及实现类信息
     * @param output       输出流
     */
    private void writeFile(Map<String, Set<String>> allImportMap, OutputStream output) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8))) {
            for (Map.Entry<String, Set<String>> entry : allImportMap.entrySet()) {
                String providerInterface = entry.getKey();
                Set<String> services = entry.getValue();
                writer.write(providerInterface);
                writer.write("=\\");
                writer.newLine();
                String[] serviceArrays = services.toArray(String[]::new);
                for (int i = 0; i < serviceArrays.length; i++) {
                    writer.write("\t");
                    writer.write(serviceArrays[i]);
                    if (i != serviceArrays.length - 1) {
                        writer.write(",\\");
                    }
                    writer.newLine();
                }
            }
            writer.newLine();
            writer.flush();
        } catch (IOException ignored) {

        }
    }
}
