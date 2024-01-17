/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.video.support;

import com.livk.commons.io.ResourceUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * <p>
 * VideoResourceHttpRequestHandler
 * </p>
 *
 * @author livk
 */
@Component
public class VideoResourceHttpRequestHandler extends ResourceHttpRequestHandler {

	public static final String ATTR = "VIDEO-FILE";

	@Override
	protected Resource getResource(HttpServletRequest request) {
		String location = (String) request.getAttribute(ATTR);
		return ResourceUtils.getResource(location);
	}

}
