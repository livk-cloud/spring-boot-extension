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

package com.livk.commons.expression.aviator;

import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * SPI test function for {@link ServiceFunctionLoaderTests}.
 *
 * @author livk
 */
@AutoService(AviatorFunction.class)
public class TestEchoFunction extends AbstractFunction {

	@Override
	public String getName() {
		return "spiEcho";
	}

	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
		return new AviatorString(arg1.stringValue(env));
	}

}
