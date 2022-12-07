package com.livk.http.example.http;

import com.livk.autoconfigure.http.annotation.Provider;
import org.springframework.web.service.annotation.GetExchange;

/**
 * <p>
 * RemoteService
 * </p>
 *
 * @author livk
 */
@Provider(url = "https://github.com/")
public interface RemoteService {

    @GetExchange()
    String get();

}
