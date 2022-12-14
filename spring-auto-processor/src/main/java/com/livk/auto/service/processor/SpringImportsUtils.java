package com.livk.auto.service.processor;

import lombok.experimental.UtilityClass;

import javax.tools.FileObject;
import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;

/**
 * <p>
 * SpringImportsUtils
 * </p>
 *
 * @author livk
 * @date 2022/12/13
 */
@UtilityClass
class SpringImportsUtils {

    public static Set<String> read(FileObject fileObject) throws IOException {
        try (BufferedReader reader = new BufferedReader(fileObject.openReader(true))) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    public static void writeFile(Collection<String> services, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
        for (String service : services) {
            writer.write(service);
            writer.newLine();
        }
        writer.flush();
    }
}
