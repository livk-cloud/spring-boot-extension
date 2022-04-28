package com.livk.interceptor;

import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 仅支持Mybatis
 * </p>
 *
 * @author livk
 * @date 2022/1/29
 */
@Slf4j
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var mappedStatement = (MappedStatement) invocation.getArgs()[0];
        var sqlCommandType = mappedStatement.getSqlCommandType();
        var parameter = invocation.getArgs()[1];
        var declaredFields = ReflectionUtils.getFields(parameter);
        if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
            for (var field : declaredFields) {
                if (field.isAnnotationPresent(SqlFunction.class)) {
                    var sqlFunction = field.getAnnotation(SqlFunction.class);
                    var value = getValue(sqlFunction);
                    if (value == null) {
                        continue;
                    }
                    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                        ReflectionUtils.set(field, parameter, value);
                    } else {
                        if (sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
                            ReflectionUtils.set(field, parameter, value);
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private Object getValue(SqlFunction sqlFunction) {
        var value = sqlFunction.time().handler();
        return value != null ? value : BeanUtils.instantiateClass(sqlFunction.supplier()).handler();
    }
}
