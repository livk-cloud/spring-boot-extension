package com.livk.commons.bean.domain;

import com.livk.commons.bean.domain.TreeNode;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.test.TestLogUtils;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TreeNodeTest {

    static TreeNode<Long, String> root;

    @Order(1)
    @Test
    void createRoot() {
        root = TreeNode.createRoot(0L, "root");
        TestLogUtils.info("{}", root);
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
        TestLogUtils.info("{}", root);
        TestLogUtils.info("{}", JacksonUtils.toJsonStr(root));
        assertNotNull(root);
    }

    @Order(3)
    @Test
    void addChild() {
        TreeNode<Long, String> node = new TreeNode<Long, String>().setId(7L).setNode("7").setPid(4L);
        root.addChild(node);
        TestLogUtils.info("{}", root);
        TestLogUtils.info("{}", JacksonUtils.toJsonStr(root));
    }

    @Order(4)
    @Test
    void findById() {
        TreeNode<Long, String> childNode = root.findById(3L);
        TestLogUtils.info("{}", childNode);
    }
}
