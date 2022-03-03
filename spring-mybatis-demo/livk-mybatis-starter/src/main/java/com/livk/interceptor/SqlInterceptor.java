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
import java.util.Map;

/**
 * <p>
 * 目前不支持MP的更新操作
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
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var mappedStatement = (MappedStatement) invocation.getArgs()[0];
        var sqlCommandType = mappedStatement.getSqlCommandType();
        var parameter = invocation.getArgs()[1];
        Field[] declaredFields = getDeclaredFields(parameter);
        if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
            for (var field : declaredFields) {
                if (field.isAnnotationPresent(SqlFunction.class)) {
                    var sqlFunction = field.getAnnotation(SqlFunction.class);
                    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                        if (sqlFunction.fill().equals(SqlFill.INSERT_UPDATE) ||
                            sqlFunction.fill().equals(SqlFill.INSERT)) {
                            this.setField(field, parameter, BeanUtils.instantiateClass(sqlFunction.supplier()));
                        }
                    } else {
                        if (sqlFunction.fill().equals(SqlFill.INSERT_UPDATE) ||
                            sqlFunction.fill().equals(SqlFill.UPDATE)) {
                            this.setField(field, parameter, BeanUtils.instantiateClass(sqlFunction.supplier()));
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private Field[] getDeclaredFields(Object parameter) {
        Field[] declaredFields;
        if (parameter instanceof Map) {
            //MP
            @SuppressWarnings("unchecked")
            Map<String, Object> paramsMap = (Map<String, Object>) parameter;
            declaredFields = paramsMap.values()
                    .stream()
                    .map(p -> p.getClass().getDeclaredFields())
                    .reduce(ArrayUtils::addAll).orElse(new Field[]{});
        } else {
            declaredFields = parameter.getClass().getDeclaredFields();
        }
        if (parameter.getClass().getSuperclass() != null) {
            var superFiled = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superFiled);
        }
        return declaredFields;
    }

    private void setField(Field field, Object parameter, FunctionHandle<?> value) throws IllegalAccessException {
        if (parameter instanceof Map paramsMap) {
            for (final Object params : paramsMap.values()) {
                set(field, params, value);
            }
        } else {
            set(field, parameter, value);
        }
    }

    private void set(Field field, Object parameter, FunctionHandle<?> value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value.handler());
    }
}
