package com.livk.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Greeting
 * </p>
 *
 * @author livk
 * @date 2021/11/24
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor(onConstructor_ = @JsonCreator)
public class Greeting extends AbstractEntity<Greeting> {

	@JsonProperty("content")
	private final String contents;

}
