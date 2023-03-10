package com.livk.auth.server.common.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * @author Livk
 */
@UtilityClass
final class KeyGeneratorUtils {

    @SneakyThrows
    KeyPair generateRsaKey() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

}
