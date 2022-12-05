package com.livk.commons.domain;

import com.livk.commons.util.JacksonUtils;
import com.livk.commons.util.LogUtils;
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

    static TreeNode<String> root;

    @Order(1)
    @Test
    void createRoot() {
        root = TreeNode.createRoot(0L, "root");
        LogUtils.info("{}", root);
        assertNotNull(root);
    }

    @Order(2)
    @Test
    void setChildren() {
        var nodes = List.of(
                new TreeNode<String>().setId(1L).setNode("1").setPid(0L),
                new TreeNode<String>().setId(2L).setNode("2").setPid(0L),
                new TreeNode<String>().setId(3L).setNode("3").setPid(1L),
                new TreeNode<String>().setId(4L).setNode("4").setPid(1L),
                new TreeNode<String>().setId(5L).setNode("5").setPid(2L),
                new TreeNode<String>().setId(6L).setNode("6").setPid(3L)
        );
        root.setChildren(nodes);
        LogUtils.info("{}", root);
        LogUtils.info("{}", JacksonUtils.toJsonStr(root));
        assertNotNull(root);
    }

    @Order(3)
    @Test
    void addChild() {
        TreeNode<String> node = new TreeNode<String>().setId(7L).setNode("7").setPid(4L);
        root.addChild(node);
        LogUtils.info("{}", root);
        LogUtils.info("{}", JacksonUtils.toJsonStr(root));
    }

    @Order(4)
    @Test
    void findById() {
        TreeNode<String> childNode = root.findById(3L);
        LogUtils.info("{}", childNode);
    }
}
