package com.livk.mail;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MailApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class MailApp {

    public static void main(String[] args) {
        LivkSpring.run(MailApp.class, args);
    }

}
