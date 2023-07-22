package com.livk.autoconfigure.http.adapter;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

/**
 * @author livk
 */
public interface AdapterFactory<H extends HttpExchangeAdapter> {

	H create(BeanFactory beanFactory);
}
