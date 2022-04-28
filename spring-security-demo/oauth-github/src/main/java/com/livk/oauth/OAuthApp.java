package com.livk.oauth;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * https://github.com/login/oauth/authorize client_id redirect_uri
 * </p>
 * <p>
 * https://github.com/login/oauth/access_token client_id client_secret code
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@SpringBootApplication
public class OAuthApp {

	public static void main(String[] args) {
		LivkSpring.run(OAuthApp.class, args);
	}

}
