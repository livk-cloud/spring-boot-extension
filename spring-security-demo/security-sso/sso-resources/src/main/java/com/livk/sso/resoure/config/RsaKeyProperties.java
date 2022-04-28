package com.livk.sso.resoure.config;

import com.livk.sso.util.RSAUtils;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

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

	public RsaKeyProperties(@Name("public-key") String publicKeyFile) {
		publicKey = RSAUtils.getPublicKey(publicKeyFile);
	}

}
