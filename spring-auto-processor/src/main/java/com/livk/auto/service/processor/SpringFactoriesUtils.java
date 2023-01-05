package com.livk.auto.service.processor;

import lombok.experimental.UtilityClass;

import javax.tools.FileObject;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Charsets.UTF_8;

/**
 * <p>
 * SpringFactoriesUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
class SpringFactoriesUtils {

    public static Set<String> read(String providerInterface, FileObject fileObject) {
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

    public static void writeFile(Map<String, Set<String>> allImportMap, OutputStream output) {
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
