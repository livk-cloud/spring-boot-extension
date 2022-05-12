package com.livk.example;

import com.livk.common.LivkSpring;
import com.livk.mapstruct.annotation.EnableConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MapstructExampleApp
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@EnableConverter
@SpringBootApplication
public class MapstructExampleApp {

	public static void main(String[] args) {
		LivkSpring.run(MapstructExampleApp.class, args);
	}

}
