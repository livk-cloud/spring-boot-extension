package com.livk.autoconfigure.disruptor.exception;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.autoconfigure.disruptor.exception.DisruptorRegistrarException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * @author livk
 */
@SpringFactories(FailureAnalyzer.class)
public class DisruptorRegistrarFailureAnalyzer extends AbstractFailureAnalyzer<DisruptorRegistrarException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, DisruptorRegistrarException cause) {
        return new FailureAnalysis(cause.getMessage(), "请添加 basePackages or basePackageClasses", cause);
    }

}
