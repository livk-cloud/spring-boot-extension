package com.livk.commons.jackson.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.jackson.support.annotation.BigDecimalFormat;
import com.livk.commons.util.ReflectionUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class BigDecimalSerializerTest {

    @Test
    public void test() {
        Big big = new Big();
        big.count = BigDecimal.valueOf(0.333333);
        big.sunCount = BigDecimal.valueOf(0.333333);
        String json = JacksonUtils.toJsonStr(big);
        Big bean = JacksonUtils.toBean(json, Big.class);
        assertNotNull(bean);
        assertEquals(0.33d, bean.count.doubleValue());
        assertEquals(0.333d, bean.sunCount.doubleValue());
        JsonNode jsonNode = JacksonUtils.readTree(json);
        assertEquals(0.33d, jsonNode.get(ReflectionUtils.getFieldName(Big::getCount)).asDouble());
        assertEquals(0.333d, jsonNode.get(ReflectionUtils.getFieldName(Big::getSunCount)).asDouble());
    }

    @EqualsAndHashCode
    @Getter
    private static class Big {
        @BigDecimalFormat
        private BigDecimal count;

        @BigDecimalFormat(pattern = "#0.000")
        private BigDecimal sunCount;
    }
}
