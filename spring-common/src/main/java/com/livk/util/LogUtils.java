package com.livk.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 在test种使用
 * </p>
 *
 * @author livk
 * @date 2022/10/12
 */
@UtilityClass
public class LogUtils {
    private static final Map<String, Logger> LOGGER_MAP = new ConcurrentHashMap<>();

    private static final String NAME = LogUtils.class.getName();

    public void info(String format, Object... arguments) {
        Logger logger = currentLogger();
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    public void trace(String format, Object... arguments) {
        Logger logger = currentLogger();
        if (logger.isTraceEnabled()) {
            logger.trace(format, arguments);
        }
    }

    public void warn(String format, Object... arguments) {
        Logger logger = currentLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(format, arguments);
        }
    }

    public void debug(String format, Object... arguments) {
        Logger logger = currentLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    public void error(String format, Object... arguments) {
        Logger logger = currentLogger();
        if (logger.isErrorEnabled()) {
            logger.error(format, arguments);
        }
    }

    private boolean notEqualsClass(StackTraceElement stackTraceElement) {
        return !stackTraceElement.getClassName().equals(NAME);
    }

    private Logger currentLogger() {
        StackTraceElement stackTraceElement = Arrays.stream(Thread.currentThread().getStackTrace())
                .skip(1)
                .filter(LogUtils::notEqualsClass)
                .findFirst()
                .orElseThrow();
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        return LOGGER_MAP.computeIfAbsent(className + "#" + methodName, LoggerFactory::getLogger);
    }
}
