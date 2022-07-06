package com.livk.aop;

import com.livk.aop.support.AnnotationAspect;
import com.livk.aop.support.ExecutionAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * AopAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@AutoConfiguration
@Import({AnnotationAspect.class, ExecutionAspect.class})
public class AopAutoConfiguration {
}
