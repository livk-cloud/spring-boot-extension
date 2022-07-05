package com.livk.aop.support;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * <p>
 * AnnotationInvoke
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@Data
@AllArgsConstructor(staticName = "of")
public class AnnotationInvoke<A extends Annotation> {
    private final A annotation;
}
