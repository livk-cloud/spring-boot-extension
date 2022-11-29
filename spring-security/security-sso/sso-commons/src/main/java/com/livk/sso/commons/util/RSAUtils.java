package com.livk.sso.commons.util;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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

    public RSAKey rsaKey(Resource resource, String password, String alise) {
        KeyPair keyPair = new KeyStoreKeyFactory(resource, password.toCharArray()).getKeyPair(alise);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    }
}
