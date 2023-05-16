/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.bean.domain;

import com.livk.commons.jackson.util.JsonMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * TreeNodeTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TreeNodeTest {

    static TreeNode<Long, String> root;

    @Order(1)
    @Test
    void createRoot() {
        root = TreeNode.createRoot(0L, "root");
        log.info("{}", root);
        assertNotNull(root);
    }

    @Order(2)
    @Test
    void setChildren() {
        var nodes = List.of(
                new TreeNode<Long, String>().setId(1L).setNode("1").setPid(0L),
                new TreeNode<Long, String>().setId(2L).setNode("2").setPid(0L),
                new TreeNode<Long, String>().setId(3L).setNode("3").setPid(1L),
                new TreeNode<Long, String>().setId(4L).setNode("4").setPid(1L),
                new TreeNode<Long, String>().setId(5L).setNode("5").setPid(2L),
                new TreeNode<Long, String>().setId(6L).setNode("6").setPid(3L)
        );
        root.setChildren(nodes);
        log.info("{}", root);
        log.info("{}", JsonMapperUtils.writeValueAsString(root));
        assertNotNull(root);
    }

    @Order(3)
    @Test
    void addChild() {
        TreeNode<Long, String> node = new TreeNode<Long, String>().setId(7L).setNode("7").setPid(4L);
        root.addChild(node);
        log.info("{}", root);
        log.info("{}", JsonMapperUtils.writeValueAsString(root));
    }

    @Order(4)
    @Test
    void findById() {
        TreeNode<Long, String> childNode = root.findById(3L);
        log.info("{}", childNode);
    }
}
