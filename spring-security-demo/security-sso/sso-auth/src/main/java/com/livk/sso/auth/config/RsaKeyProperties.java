package com.livk.sso.auth.config;

import com.livk.sso.util.RSAUtils;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * RsaKeyProperties
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Getter
@ConfigurationProperties(RsaKeyProperties.PREFIX)
public class RsaKeyProperties {

    public static final String PREFIX = "rsa.key";

    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public RsaKeyProperties(@Name("public-key") String publicKeyFile, @Name("private-key") String privateKeyFile) {
        publicKey = RSAUtils.getPublicKey(publicKeyFile);
        privateKey = RSAUtils.getPrivateKey(privateKeyFile);
    }

}
