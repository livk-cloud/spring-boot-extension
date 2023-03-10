package com.livk.autoconfigure.ip2region;

import com.google.common.net.InetAddresses;
import com.livk.autoconfigure.ip2region.annotation.IP;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * <p>
 * IPValidator
 * </p>
 *
 * @author livk
 */
public class IPValidator implements ConstraintValidator<IP, String> {

    @Override
    public boolean isValid(String ipStr, ConstraintValidatorContext context) {
        return StringUtils.hasText(ipStr) && InetAddresses.isInetAddress(ipStr);
    }

}
