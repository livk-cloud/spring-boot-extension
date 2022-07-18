package com.livk.auth.server.common.util;

import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * <p>
 * KeyGeneratorUtils
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@UtilityClass
final class KeyGeneratorUtils {

    KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}
