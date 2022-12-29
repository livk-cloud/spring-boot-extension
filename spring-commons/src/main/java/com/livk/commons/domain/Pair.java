package com.livk.commons.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * Pair
 * </p>
 *
 * @author livk
 */
@JsonSerialize(using = Pair.PairJsonSerializer.class)
@JsonDeserialize(using = Pair.PairJsonDeserializer.class)
public record Pair<K, V>(K key, V value) implements Serializable, Cloneable {

    public static final Pair<?, ?> EMPTY = Pair.of(null, null);
    @Serial
    private static final long serialVersionUID = -2303547536834226401L;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V> clone() {
        try {
            return (Pair<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    static class PairJsonSerializer extends StdScalarSerializer<Pair<Object, Object>> implements ContextualSerializer {

        protected PairJsonSerializer() {
            super(Pair.class, false);
        }

        private JsonSerializer<Object> keySerializer;

        @Override
        public void serialize(Pair<Object, Object> pair, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject(pair);
            keySerializer.serialize(pair.key(), gen, provider);
            gen.writeObject(pair.value);
            gen.writeEndObject();
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            keySerializer = prov.findKeySerializer(Object.class, property);
            return this;
        }
    }

    static class PairJsonDeserializer extends StdScalarDeserializer<Pair<Object, Object>> implements ContextualDeserializer {

        private KeyDeserializer keyDeserializer;
        private JsonDeserializer<Object> valueDeserializer;

        protected PairJsonDeserializer() {
            super(Pair.class);
        }

        @Override
        public Pair<Object, Object> deserialize(JsonParser p, DeserializationContext context) throws IOException {
            p.nextToken();
            String name = p.currentName();
            p.nextToken();
            return Pair.of(keyDeserializer.deserializeKey(name, context), valueDeserializer.deserialize(p, context));
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
            JavaType contextualType = context.getContextualType();
            TypeBindings bindings = contextualType.getBindings();
            JavaType keyType = bindings.getBoundType(0);
            JavaType valueType = bindings.getBoundType(1);
            keyDeserializer = context.findKeyDeserializer(keyType, property);
            valueDeserializer = context.findContextualValueDeserializer(valueType, property);
            return this;
        }
    }
}
