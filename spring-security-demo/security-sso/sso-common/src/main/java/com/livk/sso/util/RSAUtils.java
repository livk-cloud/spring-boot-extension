package com.livk.sso.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <p>
 * RSAUtils
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@UtilityClass
public class RSAUtils {

    private static final int DEFAULT_SIZE = 2048;
    private static KeyFactory factory;

    static {
        try {
            factory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey(String filename) {
        var bytes = readFile(filename);
        bytes = Base64.getDecoder().decode(bytes);
        var spec = new X509EncodedKeySpec(bytes);
        try {
            return factory.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey(String filename) {
        var bytes = readFile(filename);
        bytes = Base64.getDecoder().decode(bytes);
        var spec = new PKCS8EncodedKeySpec(bytes);
        try {
            return factory.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generateKey(String publicKeyFilename, String privateKeyFilename, String secret, int keySize) {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            var secureRandom = new SecureRandom(secret.getBytes());
            keyPairGenerator.initialize(Math.max(keySize, DEFAULT_SIZE), secureRandom);
            var keyPair = keyPairGenerator.genKeyPair();
            var publicKeyBytes = keyPair.getPublic().getEncoded();
            publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
            writeFile(publicKeyFilename, publicKeyBytes);

            var privateKeyBytes = keyPair.getPrivate().getEncoded();
            privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
            writeFile(privateKeyFilename, privateKeyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String destPath, byte[] bytes) {
        var file = new File(destPath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Files.write(file.toPath(), bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] readFile(String filename) {
        try {
            File file;
            if (filename.startsWith("classpath:")) {
                filename = filename.replaceFirst("classpath:", "");
                file = new ClassPathResource(filename).getFile();
            } else {
                file = new File(filename);
            }
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * 生成公钥私钥
     */
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        String privateKey = property + "/id_key_rsa";
        String publicKey = property + "/id_key_rsa.pub";
        RSAUtils.generateKey(publicKey, privateKey, "livk", 1024);
    }
}
