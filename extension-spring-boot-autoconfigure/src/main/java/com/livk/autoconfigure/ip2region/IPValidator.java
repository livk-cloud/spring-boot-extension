package com.livk.autoconfigure.ip2region;

import com.livk.autoconfigure.ip2region.annotation.IP;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * <p>
 * IPValidator
 * </p>
 *
 * @author livk
 */
public class IPValidator implements ConstraintValidator<IP, String> {

    private static final Pattern IP_MATCH = Pattern.compile("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");

    @Override
    public boolean isValid(String ipStr, ConstraintValidatorContext context) {
        if (StringUtils.hasText(ipStr)) {
            return IP_MATCH.matcher(ipStr).matches();
        }
        return false;
    }

}
