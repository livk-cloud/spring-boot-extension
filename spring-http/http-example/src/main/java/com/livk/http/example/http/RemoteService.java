package com.livk.http.example.http;

import com.livk.autoconfigure.http.annotation.BeanName;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * <p>
 * RemoteService
 * </p>
 *
 * @author livk
 */
@BeanName("remoteService")
@HttpExchange("https://cn.bing.com/")
public interface RemoteService {

    @GetExchange()
    String get();

}
