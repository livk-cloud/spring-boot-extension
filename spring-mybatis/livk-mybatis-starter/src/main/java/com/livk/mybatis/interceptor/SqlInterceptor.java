package com.livk.mybatis.interceptor;

import com.livk.mybatis.annotation.SqlFunction;
import com.livk.mybatis.enums.SqlFill;
import com.livk.util.BeanUtils;
import com.livk.util.FieldUtils;
import com.livk.util.ReflectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;

/**
 * <p>
 * 仅支持Mybatis
 * </p>
 *
 * @author livk
 * @date 2022/1/29
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        Field[] declaredFields = FieldUtils.getAllFields(parameter.getClass());
        if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SqlFunction.class)) {
                    SqlFunction sqlFunction = field.getAnnotation(SqlFunction.class);
                    Object value = getValue(sqlFunction);
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
        Object value = sqlFunction.time().handler();
        return value != null ? value : BeanUtils.instantiateClass(sqlFunction.supplier()).handler();
    }

}
