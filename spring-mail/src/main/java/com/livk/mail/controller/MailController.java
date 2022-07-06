package com.livk.mail.controller;

import com.livk.common.Pair;
import com.livk.mail.support.MailTemplate;
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

    private final MailTemplate mailTemplate;

    @PostMapping("send")
    public HttpEntity<Void> send() throws IOException, TemplateException {
        // 定义个数据根节点
        var root = new HashMap<String, Object>();
        // 往里面塞第一层节点
        root.put("UserName", "Livk-Cloud");

        var temp = new String[]{"dog", "cat", "tiger"};
        var pets = new ArrayList<String>();
        Collections.addAll(pets, temp);
        // 往里面塞个List对象
        root.put("pets", pets);

        var template = mailTemplate.getConfiguration().getTemplate("hello.ftl");
        var text = FreeMarkerTemplateUtils.processTemplateIntoString(template, root);

        mailTemplate.send(Pair.of("1375632510@qq.com", "I am Livk"), "This is subject 主题", text, root, "1375632510@qq.com");
        return ResponseEntity.ok().build();
    }

}
