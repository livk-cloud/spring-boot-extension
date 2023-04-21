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

package com.livk.commons.cglib;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ReflectUtils;

import java.security.ProtectionDomain;
import java.util.*;

/**
 * <p>The type Bean map.</p>
 * <p>根据{@link org.springframework.cglib.beans.BeanMap} 调整</p>
 * <p>依赖于spring-core</p>
 *
 * @author livk
 * @see org.springframework.cglib.beans.BeanMap
 */
public abstract class BeanMap implements Map<String, Object> {
    /**
     * Limit the properties reflected in the key set of the map
     * to readable properties.
     *
     * @see org.springframework.cglib.beans.BeanMap.Generator#setRequire org.springframework.cglib.beans.BeanMap.Generator#setRequire
     */
    public static final int REQUIRE_GETTER = 1;

    /**
     * Limit the properties reflected in the key set of the map
     * to writable properties.
     *
     * @see org.springframework.cglib.beans.BeanMap.Generator#setRequire org.springframework.cglib.beans.BeanMap.Generator#setRequire
     */
    public static final int REQUIRE_SETTER = 2;
    /**
     * The Bean.
     */
    protected Object bean;

    /**
     * Instantiates a new Bean map.
     */
    protected BeanMap() {
    }

    /**
     * Instantiates a new Bean map.
     *
     * @param bean the bean
     */
    protected BeanMap(Object bean) {
        setBean(bean);
    }

    /**
     * Helper method to create a new <code>BeanMap</code>.  For finer
     * control over the generated instance, use a new instance of
     * <code>BeanMap.Generator</code> instead of this static method.
     *
     * @param <T>  the type parameter
     * @param bean the JavaBean underlying the map
     * @return a new <code>BeanMap</code> instance
     */
    public static <T> BeanMap create(T bean) {
        Generator<T> gen = new Generator<>();
        gen.setBean(bean);
        return gen.create();
    }

    /**
     * Create a new <code>BeanMap</code> instance using the specified bean.
     * This is faster than using the {@link #create} static method.
     *
     * @param bean the JavaBean underlying the map
     * @return a new <code>BeanMap</code> instance
     */
    public abstract BeanMap newInstance(Object bean);

    /**
     * Get the type of a property.
     *
     * @param name the name of the JavaBean property
     * @return the type of the property, or null if the property does not exist
     */
    public abstract Class<?> getPropertyType(String name);

    @Override
    public Object get(Object key) {
        return get(bean, key);
    }

    @Override
    public Object put(String key, Object value) {
        return put(bean, key, value);
    }

    /**
     * Get the property of a bean. This allows a <code>BeanMap</code>
     * to be used statically for multiple beans--the bean instance tied to the
     * map is ignored and the bean passed to this method is used instead.
     *
     * @param bean the bean to query; must be compatible with the type of             this <code>BeanMap</code>
     * @param key  must be a String
     * @return the current value, or null if there is no matching property
     */
    public abstract Object get(Object bean, Object key);

    /**
     * Set the property of a bean. This allows a <code>BeanMap</code>
     * to be used statically for multiple beans--the bean instance tied to the
     * map is ignored and the bean passed to this method is used instead.
     *
     * @param bean  the bean
     * @param key   must be a String
     * @param value the value
     * @return the old value, if there was one, or null
     */
    public abstract Object put(Object bean, Object key, Object value);

    /**
     * Return the bean currently in use by this map.
     *
     * @return the current JavaBean
     * @see #setBean #setBean
     */
    public Object getBean() {
        return bean;
    }

    /**
     * Change the underlying bean this map should use.
     *
     * @param bean the new JavaBean
     * @see #getBean #getBean
     */
    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (String s : keySet()) {
            Object v = get(s);
            if (((value == null) && (v == null)) || (value != null && value.equals(v))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ?> t) {
        for (String key : t.keySet()) {
            put(key, t.get(key));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map<?, ?> other)) {
            return false;
        }
        if (size() != other.size()) {
            return false;
        }
        for (Object key : keySet()) {
            if (!other.containsKey(key)) {
                return false;
            }
            Object v1 = get(key);
            Object v2 = other.get(key);
            if (!(Objects.equals(v1, v2))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (Object key : keySet()) {
            Object value = get(key);
            code += ((key == null) ? 0 : key.hashCode()) ^
                    ((value == null) ? 0 : value.hashCode());
        }
        return code;
    }

    // TODO: optimize
    @Override
    public Set<Entry<String, Object>> entrySet() {
        HashMap<String, Object> copy = new HashMap<>();
        for (String key : keySet()) {
            copy.put(key, get(key));
        }
        return Collections.unmodifiableMap(copy).entrySet();
    }

    @Override
    public Collection<Object> values() {
        Set<String> keys = keySet();
        List<Object> values = new ArrayList<>(keys.size());
        for (String key : keys) {
            values.add(get(key));
        }
        return Collections.unmodifiableCollection(values);
    }

    /*
     * @see java.util.AbstractMap#toString
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Iterator<String> it = keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            sb.append(key);
            sb.append('=');
            sb.append(get(key));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * The type Generator.
     *
     * @param <T> the type parameter
     */
    public static class Generator<T> extends AbstractClassGenerator<T> {
        private static final Source SOURCE = new Source(BeanMap.class.getName());

        @SuppressWarnings("unchecked")
        private final BeanMapKey<T> KEY_FACTORY =
                (BeanMapKey<T>) KeyFactory.create(BeanMapKey.class, KeyFactory.CLASS_BY_NAME);
        private Object bean;
        private Class<T> beanClass;
        private int require;

        /**
         * Instantiates a new Generator.
         */
        public Generator() {
            super(SOURCE);
        }

        /**
         * Set the bean that the generated map should reflect. The bean may be swapped
         * out for another bean of the same type using {@link #setBean}.
         * Calling this method overrides any value previously set using {@link #setBeanClass}.
         * You must call either this method or {@link #setBeanClass} before {@link #create}.
         *
         * @param bean the initial bean
         */
        @SuppressWarnings("unchecked")
        public void setBean(Object bean) {
            this.bean = bean;
            if (bean != null) {
                beanClass = (Class<T>) bean.getClass();
                // SPRING PATCH BEGIN
                setContextClass(beanClass);
                // SPRING PATCH END
            }
        }

        /**
         * Set the class of the bean that the generated map should support.
         * You must call either this method or {@link #setBeanClass} before {@link #create}.
         *
         * @param beanClass the class of the bean
         */
        public void setBeanClass(Class<T> beanClass) {
            this.beanClass = beanClass;
        }

        /**
         * Limit the properties reflected by the generated map.
         *
         * @param require any combination of {@link #REQUIRE_GETTER} and                {@link #REQUIRE_SETTER}; default is zero (any property allowed)
         */
        public void setRequire(int require) {
            this.require = require;
        }

        @Override
        protected ClassLoader getDefaultClassLoader() {
            return beanClass.getClassLoader();
        }

        @Override
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(beanClass);
        }

        /**
         * Create a new instance of the <code>BeanMap</code>. An existing
         * generated class will be reused if possible.
         *
         * @return the bean map
         */
        public BeanMap create() {
            if (beanClass == null) {
                throw new IllegalArgumentException("Class of bean unknown");
            }
            setNamePrefix(beanClass.getName());
            return (BeanMap) super.create(KEY_FACTORY.newInstance(beanClass, require));
        }

        @Override
        public void generateClass(ClassVisitor v) throws Exception {
            new BeanMapEmitter(v, getClassName(), beanClass, require);
        }

        @Override
        protected Object firstInstance(Class<?> type) {
            return ((BeanMap) ReflectUtils.newInstance(type)).newInstance(bean);
        }

        @Override
        protected Object nextInstance(Object instance) {
            return ((BeanMap) instance).newInstance(bean);
        }

        /**
         * The interface Bean map key.
         *
         * @param <T> the type parameter
         */
        interface BeanMapKey<T> {
            /**
             * New instance t.
             *
             * @param type    the type
             * @param require the require
             * @return the t
             */
            T newInstance(Class<T> type, int require);
        }
    }
}
