package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.livk.commons.jackson.support.annotation.BigDecimalFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * @author livk
 */
@JsonComponent
@AllArgsConstructor
@NoArgsConstructor
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {

    private String format;

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(new DecimalFormat(format).format(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (BigDecimal.class.equals(property.getType().getRawClass())) {
            BigDecimalFormat bigDecimalFormat = Optional.ofNullable(property.getAnnotation((BigDecimalFormat.class)))
                    .orElse(property.getContextAnnotation(BigDecimalFormat.class));
            return new BigDecimalSerializer(bigDecimalFormat.pattern());
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
