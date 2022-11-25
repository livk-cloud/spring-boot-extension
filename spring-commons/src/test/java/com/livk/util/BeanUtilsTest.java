package com.livk.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * <p>
 * BeanUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/10/14
 */
class BeanUtilsTest {

    static SourceBean bean = new SourceBean("source", 10);

    static List<SourceBean> beanList = List.of(new SourceBean("source", 10),
            new SourceBean("target", 9));

    @Test
    void copy() {
        TargetBean result = BeanUtils.copy(bean, TargetBean.class);
        TargetBean targetBean = new TargetBean("source", 10);
        Assertions.assertEquals(result, targetBean);
    }

    @Test
    void copySupplier() {
        TargetBean result = BeanUtils.copy(bean, TargetBean::new);
        TargetBean targetBean = new TargetBean("source", 10);
        Assertions.assertEquals(result, targetBean);
    }

    @Test
    void copyList() {
        List<TargetBean> result = BeanUtils.copyList(beanList, TargetBean.class);
        List<TargetBean> targetBeans = List.of(new TargetBean("source", 10),
                new TargetBean("target", 9));
        Assertions.assertEquals(result, targetBeans);
    }

    @Test
    void isFieldNull() {
        boolean result = BeanUtils.isFieldNull(bean);
        Assertions.assertFalse(result);
    }
}

record SourceBean(String beanName, Integer beanNo) {
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TargetBean {
    private String beanName;
    private Integer beanNo;
}
