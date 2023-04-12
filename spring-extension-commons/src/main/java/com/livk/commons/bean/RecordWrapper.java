package com.livk.commons.bean;

/**
 * The type Record wrapper.
 *
 * @param <V> the type parameter
 * @author livk
 */
record RecordWrapper<V>(V unwrap) implements GenericWrapper<V> {

}
