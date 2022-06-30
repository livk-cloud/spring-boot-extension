package com.livk.http.example.http;

import com.livk.http.annotation.BeanName;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * <p>
 * RemoteService
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
@BeanName("remoteService")
@HttpExchange("https://cn.bing.com/")
public interface RemoteService {

    @GetExchange()
    String get();

}
