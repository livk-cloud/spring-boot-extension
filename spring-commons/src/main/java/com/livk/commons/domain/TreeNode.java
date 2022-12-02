package com.livk.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * TreeNode
 * </p>
 *
 * @author livk
 * @date 2022/11/3
 */
@Slf4j
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNode<T> {

    private Long id;

    private T node;
    private Long pid;
    private List<TreeNode<T>> children;

    public static <T> TreeNode<T> createRoot(Long id, T node) {
        return new TreeNode<>(id, node, null, new ArrayList<>());
    }

    public boolean addChild(TreeNode<T> treeNo) {
        if (this.findById(treeNo.id) != null) {
            log.info("出现相同节点 id:{}", treeNo.id);
            return false;
        }
        TreeNode<T> parent = this.findById(treeNo.pid);
        if (parent == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(parent.children)) {
            parent.children = new ArrayList<>();
        }
        return parent.children.add(treeNo);
    }

    public void setChildren(List<TreeNode<T>> nodes) {
        List<TreeNode<T>> treeNodeList = nodes.stream()
                .filter(node -> id.equals(node.pid))
                .toList();
        if (!CollectionUtils.isEmpty(treeNodeList)) {
            children = new ArrayList<>();
            children.addAll(treeNodeList);
            children.forEach(child -> child.setChildren(nodes));
        }
    }

    public TreeNode<T> findById(Long id) {
        if (this.id.equals(id)) {
            return this;
        }
        if (!CollectionUtils.isEmpty(children)) {
            for (TreeNode<T> child : children) {
                TreeNode<T> treeNo = child.findById(id);
                if (treeNo != null) {
                    return treeNo;
                }
            }
        }
        return null;
    }
}
