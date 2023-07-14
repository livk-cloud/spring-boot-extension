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

package com.livk.commons.beans;

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
 * Construct tree nodes, node ID are not allowed to be duplicated
 * </p>
 *
 * @param <I> the type parameter
 * @param <T> the type parameter
 * @author livk
 */
@Slf4j
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNode<I, T> {

	private I id;

	private T node;
	private I pid;
	private List<TreeNode<I, T>> children;

	/**
	 * Construct the root node
	 *
	 * @param <I>  the type parameter
	 * @param <T>  the type parameter
	 * @param id   the id
	 * @param node the node
	 * @return the tree node
	 */
	public static <I, T> TreeNode<I, T> createRoot(I id, T node) {
		return new TreeNode<>(id, node, null, new ArrayList<>());
	}

	/**
	 * Add a child node
	 *
	 * @param treeNo the tree no
	 * @return the boolean
	 */
	public boolean addChild(TreeNode<I, T> treeNo) {
		if (this.findById(treeNo.id) != null) {
			log.info("出现相同节点 id:{}", treeNo.id);
			return false;
		}
		TreeNode<I, T> parent = this.findById(treeNo.pid);
		if (parent == null) {
			return false;
		}
		if (CollectionUtils.isEmpty(parent.children)) {
			parent.children = new ArrayList<>();
		}
		return parent.children.add(treeNo);
	}

	/**
	 * Set up child nodes
	 *
	 * @param nodes the nodes
	 */
	public void setChildren(List<TreeNode<I, T>> nodes) {
		List<TreeNode<I, T>> treeNodeList = nodes.stream()
			.filter(node -> id.equals(node.pid))
			.toList();
		if (!CollectionUtils.isEmpty(treeNodeList)) {
			children = new ArrayList<>();
			children.addAll(treeNodeList);
			children.forEach(child -> child.setChildren(nodes));
		}
	}

	/**
	 * Find by id tree node.
	 *
	 * @param id the id
	 * @return the tree node
	 */
	public TreeNode<I, T> findById(I id) {
		if (this.id.equals(id)) {
			return this;
		}
		if (!CollectionUtils.isEmpty(children)) {
			for (TreeNode<I, T> child : children) {
				TreeNode<I, T> treeNo = child.findById(id);
				if (treeNo != null) {
					return treeNo;
				}
			}
		}
		return null;
	}
}
