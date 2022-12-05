package com.livk.commons.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

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

    static class PairJsonSerializer extends StdScalarSerializer<Pair<?, ?>> {

        protected PairJsonSerializer() {
            super(Pair.class, false);
        }

        @Override
        public void serialize(Pair<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject(value);
            gen.writeObjectField(value.key.toString(), value.value);
            gen.writeEndObject();
        }
    }

    static class PairJsonDeserializer extends StdScalarDeserializer<Pair<?, ?>> {

        protected PairJsonDeserializer() {
            super(Pair.class);
        }

        @Override
        public Pair<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = ctxt.readTree(p);
            Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
            if (iterator.hasNext()) {
                Map.Entry<String, JsonNode> next = iterator.next();
                return Pair.of(next.getKey(), next.getValue());
            }
            return Pair.EMPTY;
        }
    }
}
