package com.livk.auto.service.processor;

import lombok.experimental.UtilityClass;

import javax.tools.FileObject;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Charsets.UTF_8;

/**
 * <p>
 * SpringFactoriesUtils
 * </p>
 *
 * @author livk
 * @date 2022/12/13
 */
@UtilityClass
class SpringFactoriesUtils {

    /**
     * 暂定不扫描，后续考虑如何实现
     */
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
                    providers.add(line.replaceAll(",\\\\", ""));
                }
            }
            return providers;
        } catch (IOException e) {
            return Set.of();
        }
    }

    public static void writeFile(String providerInterface, Collection<String> services, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
        writer.write(providerInterface);
        writer.write("=\\");
        writer.newLine();
        String[] serviceArrays = services.toArray(String[]::new);
        for (int i = 0; i < serviceArrays.length; i++) {
            writer.write(serviceArrays[i]);
            if (i != serviceArrays.length - 1) {
                writer.write(",\\");
            }
            writer.newLine();
        }
        writer.newLine();
        writer.flush();
    }
}
