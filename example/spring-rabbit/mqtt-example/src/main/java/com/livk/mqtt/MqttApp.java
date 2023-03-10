package com.livk.mqtt;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MqttApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class MqttApp {

    public static void main(String[] args) {
        SpringLauncher.run(MqttApp.class, args);
    }

}
