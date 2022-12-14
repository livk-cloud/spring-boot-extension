package com.livk.auto.service.processor;

import lombok.experimental.UtilityClass;

import javax.tools.FileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
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
    public static Set<String> read(FileObject fileObject) throws IOException {
        return Set.of();
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
