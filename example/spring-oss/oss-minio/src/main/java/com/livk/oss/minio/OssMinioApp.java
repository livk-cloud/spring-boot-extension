package com.livk.oss.minio;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class OssMinioApp {
    public static void main(String[] args) {
        SpringLauncher.run(OssMinioApp.class, args);
    }
}
