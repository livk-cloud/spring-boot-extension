package com.livk.mail;

import com.livk.commons.util.DateUtils;
import com.livk.mail.util.FreemarkerUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * MailTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@SpringBootTest
public class MailTest {

    @Autowired
    private Configuration configuration;

    @Autowired
    private JavaMailSender sender;

    @Test
    @DisplayName("测试freemarker")
    public void test() throws Exception {
        // 定义个数据根节点
        Map<String, Object> root = new HashMap<>();
        // 往里面塞第一层节点
        root.put("UserName", "Livk-Cloud");

        String[] temp = new String[]{"dog", "cat", "tiger"};
        List<String> pets = new ArrayList<>();
        Collections.addAll(pets, temp);
        // 往里面塞个List对象
        root.put("pets", pets);

        Template template = configuration.getTemplate("hello.ftl");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, root);

        MimeMessage mimeMessage = sender.createMimeMessage();
        /* 设置邮件重要性级别 */
        mimeMessage.setHeader("Importance", "High");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom("1375632510@qq.com", "I am Livk");
        helper.setTo("1375632510@qq.com");
        helper.setSubject("This is subject 主题");
        helper.setText(text, true);
//        sender.send(mimeMessage);
    }

    @Test
    public void testTemplate() throws Exception {
        Map<String, Object> root = Map.of("bucketName", "Livk-Bucket");

        Template template = configuration.getTemplate("ac.ftl");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, root);
        System.out.println(text);
        assertNotNull(text);
    }

    @Test
    public void test1() throws IOException, TemplateException {
        String txt = "${logo} -> ${code}";
        Map<String, Object> map = Map.of("logo", "www.baidu.com", "code", "123456");
        Template template = new Template("template", new StringReader(txt), configuration);
        String result = "www.baidu.com -> 123456";
        String s1 = FreemarkerUtils.processTemplateIntoString(template, map);
        log.info("processTemplateIntoString result:{}", s1);
        assertEquals(result, s1);
        String s2 = FreemarkerUtils.parse(txt, map);
        log.info("parse result:{}", s2);
        assertEquals(result, s2);
    }

    @Test
    public void testSql() {
        String sql = "INSERT INTO ${tableName}(${columns}) VALUES <#list valuesArray as values>(${values})<#if values_has_next>,</#if></#list>";
        String columns = String.join(",", "user_name", "sex", "age", "address", "status", "create_time", "update_time");
        String format = DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS);
        String values = String.join(",", "livk", "1", "26", "shenzhen", "1", format, format);
        String values2 = String.join(",", "livk", "1", "26", "shenzhen", "1", format, format);
        Map<String, Object> map = Map.of("tableName", "sys_user",
                "columns", columns,
                "valuesArray", List.of(values, values2));
        String resultSql = "INSERT INTO sys_user(user_name,sex,age,address,status,create_time,update_time) VALUES (livk,1,26,shenzhen,1," + format + "," + format + "),(livk,1,26,shenzhen,1," + format + "," + format + ")";
        String parse = parse(sql, map);
        log.info("parse result:{}", parse);
        assertEquals(resultSql, parse);
    }

    public String parse(String freemarker, Map<String, Object> model) {
        try (StringWriter out = new StringWriter()) {
            new Template("template", new StringReader(freemarker), configuration).process(model, out);
            return out.toString();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return "";
        }
    }


    public String parseFtlContent(String content, Map<String, Object> model) {
        // 获取配置
        StringWriter out = new StringWriter();
        try {
            new Template("template", new StringReader(content), configuration).process(model, out);
        } catch (TemplateException | IOException e) {
            return "";
        }
        String htmlContent = out.toString();

        try {
            out.close();
        } catch (IOException e) {
            return "";
        }
        return htmlContent;
    }
}
