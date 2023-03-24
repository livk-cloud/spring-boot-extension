package com.livk.sso.commons;

import com.livk.commons.io.ResourceUtils;
import com.livk.sso.commons.util.RSAUtils;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;

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

    private static final String DEFAULT_LOCATION = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/jwt.jks";

    private final RSAKey rsaKey;

    public RsaKeyProperties(@Name("location") String location,
                            @Name("password") String password,
                            @Name("alias") String alias) {
        if (!StringUtils.hasText(location)) {
            location = DEFAULT_LOCATION;
        }
        Resource jksResource = ResourceUtils.getResource(location);
        if (!jksResource.exists()) {
            try {
                Resource[] resources = ResourceUtils.getResources(location);
                if (resources != null) {
                    jksResource = resources[0];
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        rsaKey = RSAUtils.rsaKey(jksResource, password, alias);
    }

    public RSAKey rsaKey() {
        return rsaKey;
    }
}
