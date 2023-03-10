package com.livk.mybatis.tree.example.controller;

import com.livk.mybatis.tree.example.entity.Menu;
import com.livk.mybatis.tree.example.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * MenuController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuMapper menuMapper;

    @GetMapping
    public HttpEntity<List<Menu>> list() {
        return ResponseEntity.ok(menuMapper.list());
    }
}
