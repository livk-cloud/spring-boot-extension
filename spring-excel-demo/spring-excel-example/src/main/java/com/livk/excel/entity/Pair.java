package com.livk.excel.entity;

import java.util.Map;

/**
 * <p>
 * Pair
 * </p>
 *
 * @author livk
 * @date 2022/1/17
 */
public class Pair<K, V> implements Map.Entry<K, V> {

	private K key;

	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public Pair() {

	}

	@Override
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		this.value = value;
		return value;
	}

}
