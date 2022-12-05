package com.livk.sso.commons;

import com.livk.sso.commons.util.RSAUtils;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * <p>
 * RsaKeyProperties
 * </p>
 *
 * @author livk
 */
@ConfigurationProperties(RsaKeyProperties.PREFIX)
public class RsaKeyProperties {

    public static final String PREFIX = "rsa.key.jks";

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final RSAKey rsaKey;

    public RsaKeyProperties(@Name("location") String location,
                            @Name("password") String password,
                            @Name("alias") String alias) {
        Resource jksResource = resourceResolver.getResource(location);
        rsaKey = RSAUtils.rsaKey(jksResource, password, alias);
    }

    public RSAKey rsaKey() {
        return rsaKey;
    }
}
