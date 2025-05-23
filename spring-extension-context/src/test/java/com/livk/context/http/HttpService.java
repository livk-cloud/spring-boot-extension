package com.livk.context.http;

import com.livk.context.http.annotation.HttpProvider;
import com.livk.context.http.factory.AdapterType;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

/**
 * @author livk
 */
@HttpProvider(type = AdapterType.AUTO, url = "http://localhost:${server.port:8080}/rpc")
public interface HttpService {

	@GetExchange("java")
	Map<String, String> java();

}
