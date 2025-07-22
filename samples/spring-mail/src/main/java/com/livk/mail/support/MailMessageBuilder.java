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

package com.livk.mail.support;

import com.livk.commons.util.Pair;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author livk
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailMessageBuilder {

	private MimeMessageHelper mimeMessageHelper;

	public static MailMessageBuilder builder(JavaMailSender javaMailSender) {
		try {
			MailMessageBuilder builder = new MailMessageBuilder();
			builder.mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
			return builder;
		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MailMessageBuilder from(Pair<String, String> from) {
		try {
			mimeMessageHelper.setFrom(from.key(), from.value());
			return this;
		}
		catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public MailMessageBuilder to(String... to) {
		if (to == null || to.length < 1) {
			throw new IllegalArgumentException();
		}
		try {
			mimeMessageHelper.setTo(to);
			return this;
		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MailMessageBuilder subject(String subject) {
		try {
			mimeMessageHelper.setSubject(subject);
			return this;
		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MailMessageBuilder text(String text, boolean isHtml) {
		try {
			mimeMessageHelper.setText(text, isHtml);
			return this;
		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MailMessageBuilder file(File file) {
		try {
			if (file != null) {
				mimeMessageHelper.addAttachment(file.getName(), file);
			}
			return this;
		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MimeMessage build() {
		return mimeMessageHelper.getMimeMessage();
	}

}
