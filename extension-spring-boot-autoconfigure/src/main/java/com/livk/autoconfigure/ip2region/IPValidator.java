package com.livk.autoconfigure.ip2region;

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
 *
 */
public class IPValidator implements ConstraintValidator<IP, String> {

    @Override
    public boolean isValid(String ipStr, ConstraintValidatorContext context) {
        if (StringUtils.hasText(ipStr)) {
            if (ipStr.length() >= 7 && ipStr.length() <= 15) {
                String[] ipArray = ipStr.split("\\.");
                if (ipArray.length == 4) {
                    for (String s : ipArray) {
                        try {
                            int number = Integer.parseInt(s);
                            //4.判断每段数字是否都在0-255之间
                            if (number < 0 || number > 255) {
                                return false;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
