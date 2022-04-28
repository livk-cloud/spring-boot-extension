package com.livk.security.util;

import com.livk.util.JacksonUtils;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * ResponseUtils
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@UtilityClass
public class ResponseUtils {

    public void out(HttpServletResponse response, Map<?, ?> data) {
        response.setContentType("application/json;charset=utf-8");
        try (var writer = response.getWriter()) {
            writer.write(JacksonUtils.toJsonStr(data));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
