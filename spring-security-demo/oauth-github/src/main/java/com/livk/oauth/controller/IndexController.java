package com.livk.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * IndexController
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@RestController
public class IndexController {

	@GetMapping("index")
	public String index() {
		return "index";
	}

}
