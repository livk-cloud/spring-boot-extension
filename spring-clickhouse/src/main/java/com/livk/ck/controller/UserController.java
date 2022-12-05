package com.livk.ck.controller;

import com.livk.ck.entity.User;
import com.livk.ck.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public HttpEntity<List<User>> list() {
        return ResponseEntity.ok(userService.list());
    }

    @DeleteMapping("/{regTime}")
    public HttpEntity<Boolean> remove(@PathVariable String regTime) {
        return ResponseEntity.ok(userService.remove(regTime));
    }

    @PostMapping
    public HttpEntity<Boolean> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
}
