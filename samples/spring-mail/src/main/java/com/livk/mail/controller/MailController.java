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

package com.livk.mail.controller;

import com.livk.commons.util.Pair;
import com.livk.mail.support.MailTemplate;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author livk
 */
@RequestMapping("mail")
@RestController
@RequiredArgsConstructor
public class MailController {

	private final MailTemplate mailTemplate;

	@PostMapping("send")
	public HttpEntity<Void> send() throws IOException, TemplateException {
		// 定义个数据根节点
		Map<String, Object> root = new HashMap<>();
		// 往里面塞第一层节点
		root.put("UserName", "Livk-Cloud");

		String[] temp = new String[] { "dog", "cat", "tiger" };
		List<String> pets = new ArrayList<>();
		Collections.addAll(pets, temp);
		// 往里面塞个List对象
		root.put("pets", pets);

		Template template = mailTemplate.getConfiguration().getTemplate("hello.ftl");
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, root);

		mailTemplate.send(Pair.of("1375632510@qq.com", "I am Livk"), "This is subject 主题", text, root,
				"1375632510@qq.com");
		return ResponseEntity.ok().build();
	}

}
