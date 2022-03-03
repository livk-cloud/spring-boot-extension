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

        var declaredFields = parameter.getClass().getDeclaredFields();
        if (parameter.getClass().getSuperclass() != null) {
            var superFiled = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superFiled);
        }
        if (SqlCommandType.INSERT.equals(sqlCommandType) ||
            SqlCommandType.UPDATE.equals(sqlCommandType)) {
            for (var field : declaredFields) {
                var sqlFunction = field.getAnnotation(SqlFunction.class);
                if (sqlFunction != null) {
                    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                        if (sqlFunction.fill().equals(SqlFill.INSERT) ||
                            sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
                            this.set(field, parameter, BeanUtils.instantiateClass(sqlFunction.supplier()));
                        }
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

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    private void set(Field field, Object parameter, FunctionHandle<?> value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(parameter, value.handler());
    }
}
