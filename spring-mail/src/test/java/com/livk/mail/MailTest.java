package com.livk.mail;

import com.google.common.io.Resources;
import com.livk.mail.util.FreemarkerUtils;
import com.livk.util.DateUtils;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * MailTest
 * </p>
 *
 * @author livk
 * @date 2022/2/8
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
        sender.send(mimeMessage);
    }

    @Test
    @DisplayName("测试freemarker")
    public void testUrl() throws Exception {
        // 定义个数据根节点
        Map<String, Object> root = new HashMap<>();
        // 往里面塞第一层节点
        root.put("UserName", "Livk-Cloud");

        String[] temp = new String[]{"dog", "cat", "tiger"};
        List<String> pets = new ArrayList<>();
        Collections.addAll(pets, temp);
        // 往里面塞个List对象
        root.put("pets", pets);
        root.put("logoUrl", "http://159.75.36.73:9000/taas-test/q18.jpg");
        root.put("iosUrl", "https://www.baidu.com");

        URL url = new URL("http://159.75.36.73:9000/taas-test/admin/hello.ftl?Content-Disposition=attachment%3B%20filename%3D%22admin%2Fhello.ftl%22&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=mc-taas%2F20220728%2F%2Fs3%2Faws4_request&X-Amz-Date=20220728T071519Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=bfa10c598055cf8434f85f57e1e00c276262dcf9a27a25c40ad98c20f36c549f");
        String txt = Resources.toString(url, StandardCharsets.UTF_8);
        String parse = parse(txt, root);
        System.out.println(parse);
    }

    @Test
    public void test1() throws IOException, TemplateException {
        String txt = "${logo} -> ${code}";
        Map<String, Object> map = Map.of("logo", "www.baidu.com", "code", "123456");
        Template template = new Template("template", new StringReader(txt), configuration);
        System.out.println(FreemarkerUtils.processTemplateIntoString(template, map));
        System.out.println(FreemarkerUtils.parse(txt, map));
    }

    @Test
    public void testSql() {
        String sql = """
                INSERT INTO ${tableName}(${columns})
                VALUES 
                <#list valuesArray as values>
                (${values})
                <#if values_has_next>
                ,
                </#if>
                </#list>
                """;
        String columns = String.join(",", "user_name", "sex", "age", "address", "status", "create_time", "update_time");
        String format = DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS);
        String values = String.join(",", "livk", "1", "26", "shenzhen", "1", format, format);
        String values2 = String.join(",", "livk", "1", "26", "shenzhen", "1", format, format);
        Map<String, Object> map = Map.of("tableName", "sys_user",
                "columns", columns,
                "valuesArray", List.of(values, values2));
        System.out.println(parse(sql, map));
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
