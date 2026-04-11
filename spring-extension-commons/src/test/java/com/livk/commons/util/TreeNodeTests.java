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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TreeNodeTests {

	TreeNode<Long, String> root;

	@BeforeEach
	void setUp() {
		root = TreeNode.createRoot(0L, "root");
		var nodes = List.of(new TreeNode<Long, String>(1L).setNode("1").setPid(0L),
				new TreeNode<Long, String>(2L).setNode("2").setPid(0L),
				new TreeNode<Long, String>(3L).setNode("3").setPid(1L),
				new TreeNode<Long, String>(4L).setNode("4").setPid(1L),
				new TreeNode<Long, String>(5L).setNode("5").setPid(2L),
				new TreeNode<Long, String>(6L).setNode("6").setPid(3L));
		root.setChildren(nodes);
	}

	@Test
	void createRoot() {
		TreeNode<Long, String> newRoot = TreeNode.createRoot(0L, "root");
		assertThat(newRoot).isNotNull();
		assertThat(newRoot.getId()).isEqualTo(0L);
		assertThat(newRoot.getNode()).isEqualTo("root");
		assertThat(newRoot.getChildren()).isNotNull().isEmpty();
	}

	@Test
	void setChildren() {
		assertThat(root.getChildren()).isNotNull().hasSize(2);
		assertThat(root.getChildren().get(0).getId()).isEqualTo(1L);
		assertThat(root.getChildren().get(1).getId()).isEqualTo(2L);

		TreeNode<Long, String> child1 = root.findById(1L);
		assertThat(child1).isNotNull();
		assertThat(child1.getChildren()).hasSize(2);
	}

	@Test
	void addChild() {
		TreeNode<Long, String> node = new TreeNode<Long, String>(7L).setNode("7").setPid(4L);
		boolean added = root.addChild(node);
		assertThat(added).isTrue();

		TreeNode<Long, String> found = root.findById(7L);
		assertThat(found).isNotNull();
		assertThat(found.getNode()).isEqualTo("7");
	}

	@Test
	void addChildWithDuplicateIdReturnsFalse() {
		TreeNode<Long, String> duplicate = new TreeNode<Long, String>(3L).setNode("dup").setPid(1L);
		boolean added = root.addChild(duplicate);
		assertThat(added).isFalse();
	}

	@Test
	void addChildWithInvalidParentReturnsFalse() {
		TreeNode<Long, String> orphan = new TreeNode<Long, String>(99L).setNode("orphan").setPid(999L);
		boolean added = root.addChild(orphan);
		assertThat(added).isFalse();
	}

	@Test
	void findById() {
		TreeNode<Long, String> childNode = root.findById(3L);
		assertThat(childNode).isNotNull();
		assertThat(childNode.getNode()).isEqualTo("3");
		assertThat(childNode.getPid()).isEqualTo(1L);
	}

	@Test
	void findByIdReturnsNullForMissingId() {
		TreeNode<Long, String> missing = root.findById(999L);
		assertThat(missing).isNull();
	}

}
