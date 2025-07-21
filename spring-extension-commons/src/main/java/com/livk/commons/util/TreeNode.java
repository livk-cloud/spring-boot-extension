/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 树形检点,无法出现相同的ID节点
 * </p>
 *
 * @param <I> 树形节点ID相关type
 * @param <T> 树形节点数据相关type
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

	private static final Map<Object, TreeNode<?, ?>> idCache = new HashMap<>(); // 节点 ID

	// 缓存

	/**
	 * 创建一个root树形节点
	 * @param <I> 树形节点ID相关type
	 * @param <T> 树形节点数据相关type
	 * @param id id
	 * @param node node
	 * @return tree node
	 */
	public static <I, T> TreeNode<I, T> createRoot(I id, T node) {
		return new TreeNode<>(id, node, null, new ArrayList<>());
	}

	/**
	 * 添加一个子节点,出现相同ID则无法添加
	 * @param treeNo treeNode
	 * @return boolean
	 */
	public boolean addChild(TreeNode<I, T> treeNo) {
		if (this.findById(treeNo.id) != null) {
			log.info("The same node appears id:{}", treeNo.id);
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
	 * 设置子节点,会根据父子关系进行自动匹配
	 * @param nodes treeNode List
	 */
	public void setChildren(List<TreeNode<I, T>> nodes) {
		List<TreeNode<I, T>> treeNodeList = nodes.stream().filter(node -> id.equals(node.pid)).toList();
		if (!CollectionUtils.isEmpty(treeNodeList)) {
			children = new ArrayList<>(treeNodeList);
			children.forEach(child -> child.setChildren(nodes));
		}
	}

	/**
	 * 根据ID查找一个节点
	 * @param id id
	 * @return tree node
	 */
	@SuppressWarnings("unchecked")
	public TreeNode<I, T> findById(I id) {
		if (idCache.containsKey(id)) {
			return (TreeNode<I, T>) idCache.get(id);
		}
		if (this.id.equals(id)) {
			idCache.put(id, this);
			return this;
		}
		if (!CollectionUtils.isEmpty(children)) {
			for (TreeNode<I, T> child : children) {
				TreeNode<I, T> treeNo = child.findById(id);
				if (treeNo != null) {
					idCache.put(id, treeNo);
					return treeNo;
				}
			}
		}
		return null;
	}

}
