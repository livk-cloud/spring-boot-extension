package com.livk.sso.commons.util;

import com.livk.sso.commons.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author livk
 */
@UtilityClass
public class SecurityContextUtils {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser() {
        Object principal = getAuthentication().getPrincipal();
        return principal instanceof User user ? user : null;
    }
}
