package com.livk.autoconfigure.mybatis.interceptor;

import com.livk.autoconfigure.mybatis.annotation.SqlFunction;
import com.livk.autoconfigure.mybatis.enums.SqlFill;
import com.livk.commons.bean.util.BeanUtils;
import com.livk.commons.util.ReflectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * 仅支持Mybatis
 * </p>
 *
 * @author livk
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlDataInjection implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        List<Field> declaredFields = ReflectionUtils.getAllFields(parameter.getClass());
        if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SqlFunction.class)) {
                    SqlFunction sqlFunction = field.getAnnotation(SqlFunction.class);
                    Object value = getValue(sqlFunction);
                    if (value == null) {
                        continue;
                    }
                    //insert或者update并且SqlFill.INSERT_UPDATE
                    if (SqlCommandType.INSERT.equals(sqlCommandType) ||
                        sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
                        Method writeMethod = ReflectionUtils.getWriteMethod(parameter.getClass(), field);
                        writeMethod.invoke(parameter, value);
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
