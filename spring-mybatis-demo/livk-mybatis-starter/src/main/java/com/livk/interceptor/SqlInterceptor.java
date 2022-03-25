package com.livk.interceptor;

import com.livk.annotation.SqlFunction;
import com.livk.enums.SqlFill;
import com.livk.handler.FunctionHandle;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;

/**
 * <p>
 * 仅支持Mybatis
 * </p>
 *
 * @author livk
 * @date 2022/1/29
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlInterceptor implements AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var mappedStatement = (MappedStatement) invocation.getArgs()[0];
        var sqlCommandType = mappedStatement.getSqlCommandType();
        var parameter = invocation.getArgs()[1];
        var declaredFields = getFields(parameter);
        if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
            for (var field : declaredFields) {
                if (field.isAnnotationPresent(SqlFunction.class)) {
                    var sqlFunction = field.getAnnotation(SqlFunction.class);
                    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                        this.set(field, parameter, BeanUtils.instantiateClass(sqlFunction.supplier()));
                    } else {
                        if (sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
                            this.set(field, parameter, BeanUtils.instantiateClass(sqlFunction.supplier()));
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private Field[] getFields(Object parameter) {
        var declaredFields = parameter.getClass().getDeclaredFields();
        if (parameter.getClass().getSuperclass() != null) {
            var superFiled = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superFiled);
        }
        return declaredFields;
    }

    private void set(Field field, Object parameter, FunctionHandle<?> value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value.handler());
    }
}
