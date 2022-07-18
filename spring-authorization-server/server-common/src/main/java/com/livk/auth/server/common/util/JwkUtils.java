package com.livk.auth.server.common.util;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.experimental.UtilityClass;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * <p>
 * JwkUtils
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@UtilityClass
public class JwkUtils {

    public RSAKey generateRsa() {
        var keyPair = KeyGeneratorUtils.generateRsaKey();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

}
