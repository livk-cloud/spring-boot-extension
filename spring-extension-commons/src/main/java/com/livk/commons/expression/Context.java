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

package com.livk.commons.expression;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Context.
 *
 * @author livk
 */
public class Context implements Map<String, Object> {

    private final Map<String, Object> variables;

    /**
     * Instantiates a new Context.
     */
    public Context() {
        variables = new HashMap<>();
    }

    /**
     * Instantiates a new Context.
     *
     * @param map the map
     */
    public Context(Map<String, ?> map) {
        variables = Maps.newHashMap(map);
    }


    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return variables.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return variables.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return variables.get(key);
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        return put(key, value, false);
    }

    /**
     * Put object.
     *
     * @param key      the key
     * @param value    the value
     * @param override the override
     * @return the object
     */
    public Object put(String key, Object value, boolean override) {
        if (!override) {
            if (variables.containsKey(key)) {
                variables.get(key);
            }
        }
        return variables.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return variables.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {

    }

    /**
     * Put all.
     *
     * @param m        the m
     * @param override the override
     */
    public void putAll(Map<? extends String, ?> m, boolean override) {
        if (override) {
            variables.putAll(m);
        } else {
            for (Entry<? extends String, ?> entry : m.entrySet()) {
                if (!variables.containsKey(entry.getKey())) {
                    variables.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    public void clear() {
        variables.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return variables.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return variables.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return variables.entrySet();
    }
}
