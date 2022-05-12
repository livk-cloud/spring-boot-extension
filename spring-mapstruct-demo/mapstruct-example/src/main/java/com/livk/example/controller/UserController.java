package com.livk.example.controller;

import com.livk.example.entity.User;
import com.livk.example.entity.UserVO;
import com.livk.mapstruct.MapstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2022/5/12
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

	private final MapstructService service;

	public static final List<User> USERS = List.of(
			new User().setId(1).setUsername("livk").setPassword("123456").setType(1).setCreateTime(new Date()),
			new User().setId(2).setUsername("livk2").setPassword("123456").setType(2).setCreateTime(new Date()),
			new User().setId(3).setUsername("livk3").setPassword("123456").setType(3).setCreateTime(new Date()));

	@GetMapping
	public HttpEntity<List<UserVO>> list() {
		return ResponseEntity.ok(service.converter(USERS, UserVO.class).toList());
	}

}
