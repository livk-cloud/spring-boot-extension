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

package com.livk.context.elasticsearch.entity;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.livk.context.elasticsearch.annotation.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search {

	private Highlight highlight;

	private Integer pageNo = 1;

	private Integer pageSize = 10;

	private Query query;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Highlight {

		private List<String> preTags = List.of(Constants.PRE_TAGS);

		private List<String> postTags = List.of(Constants.POST_TAGS);

		private boolean requireFieldMatch;

		private Set<HighlightField> fields;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HighlightField {

		private String name;

		private Integer numberOfFragments = 0;

		private Integer fragmentSize = 0;

	}

}
