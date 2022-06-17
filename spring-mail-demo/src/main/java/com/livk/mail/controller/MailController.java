package com.livk.mail.controller;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * <p>
 * MailController
 * </p>
 *
 * @author livk
 * @date 2022/2/8
 */
@RequestMapping("mail")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final Configuration configuration;

    private final JavaMailSender sender;

    @PostMapping("send")
    public HttpEntity<Void> send() throws IOException, TemplateException, MessagingException {
        // 定义个数据根节点
        var root = new HashMap<String, Object>();
        // 往里面塞第一层节点
        root.put("UserName", "Livk-Cloud");

        var temp = new String[]{"dog", "cat", "tiger"};
        var pets = new ArrayList<String>();
        Collections.addAll(pets, temp);
        // 往里面塞个List对象
        root.put("pets", pets);

        var template = configuration.getTemplate("hello.ftl");
        var text = FreeMarkerTemplateUtils.processTemplateIntoString(template, root);

        var mimeMessage = sender.createMimeMessage();
        mimeMessage.setHeader("Importance", "High");
        var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom("1375632510@qq.com", "I am Livk");
        helper.setTo("1375632510@qq.com");
        helper.setSubject("This is subject 主题");
        helper.setText(text, true);
        sender.send(mimeMessage);
        return ResponseEntity.ok().build();
    }

}
