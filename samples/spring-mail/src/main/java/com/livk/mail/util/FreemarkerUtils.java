/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.mail.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.StringReader;
import java.util.Map;

/**
 * @author livk
 */
@Slf4j
@UtilityClass
public class FreemarkerUtils extends FreeMarkerTemplateUtils {

	private static final Configuration CONFIGURATION = new Configuration(
			Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

	private static final String TEMPLATE_NAME = "template";

	public String parse(String freemarker, Map<String, Object> model) {
		return parse(TEMPLATE_NAME, freemarker, model);
	}

	public String parse(String templateName, String freemarker, Map<String, Object> model) {
		try (StringReader reader = new StringReader(freemarker)) {
			Template template = new Template(templateName, reader, CONFIGURATION);
			return processTemplateIntoString(template, model);
		}
		catch (Exception e) {
			log.error("{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
