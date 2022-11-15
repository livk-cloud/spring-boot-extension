package com.livk.mybatis.tree.example.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Menu
 * </p>
 *
 * @author livk
 * @date 2022/11/15
 */
@Data
public class Menu {
    private Integer id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;
}
