package com.livk.auto.service.processor;

import lombok.experimental.UtilityClass;

import javax.tools.FileObject;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;

/**
 * <p>
 * SpringImportsUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
class SpringImportsUtils {

    public static Set<String> read(FileObject fileObject) {
        try (BufferedReader reader = new BufferedReader(fileObject.openReader(true))) {
            return reader.lines()
                    .map(String::trim)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (IOException ignored) {
            return Collections.emptySet();
        }
    }

    public static void writeFile(Collection<String> services, OutputStream output) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8))) {
            for (String service : services) {
                writer.write(service);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ignored) {

        }
    }
}
