package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 * ProxyUtils
 * </p>
 *
 * @author livk
 * @date 2022/8/1
 */
@UtilityClass
public class ProxyUtils {

    private static final Enhancer ENHANCER = new Enhancer();

    /**
     * 构建jdk代理对象
     *
     * @param obj            当前对象
     * @param interfaceClass 构建代理对象的接口class
     * @param <T>            当前对象class
     * @param <I>            构建代理对象的接口class
     * @return jdk代理对象proxy
     */
    public <T extends I, I> I JdkProxy(T obj, Class<I> interfaceClass) {
        return JdkProxy(obj, interfaceClass, JdkProxyHandler.create(obj));
    }

    /**
     * 构建jdk代理对象
     *
     * @param obj               当前对象
     * @param interfaceClass    构建代理对象的接口class
     * @param invocationHandler jdk代理处理器
     * @param <T>               当前对象class
     * @param <I>               构建代理对象的接口class
     * @return jdk代理对象proxy
     */
    @SuppressWarnings("unchecked")
    public <T extends I, I> I JdkProxy(T obj, Class<I> interfaceClass, InvocationHandler invocationHandler) {
        return interfaceClass.isInterface() ?
                (I) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                        new Class[]{interfaceClass},
                        invocationHandler) :
                obj;
    }

    /**
     * 构建spring cglib代理对象
     *
     * @param obj 当前对象
     * @param <T> 当前对象class
     * @return spring cglib代理对象
     */
    public <T> T CglibProxy(T obj) {
        return CglibProxy(obj, CglibProxyHandler.create());
    }

    /**
     * 构建spring cglib代理对象
     *
     * @param obj               当前对象
     * @param methodInterceptor spring cglib代理处理器
     * @param <T>               当前对象class
     * @return spring cglib代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T CglibProxy(T obj, MethodInterceptor methodInterceptor) {
        ENHANCER.setSuperclass(obj.getClass());
        ENHANCER.setCallback(methodInterceptor);
        return (T) ENHANCER.create();
    }

    /**
     * jdk代理处理器
     */
    public abstract class JdkProxyHandler implements InvocationHandler {
        private final Object obj;

        public JdkProxyHandler(Object obj) {
            this.obj = obj;
        }

        public static JdkProxyHandler create(Object obj) {
            return new JdkProxyHandler(obj) {
            };
        }

        /**
         * jdk默认代理
         *
         * @param proxy  proxy
         * @param method method
         * @param args   args
         * @return object
         * @throws Throwable exception
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            return method.invoke(obj, args);
        }
    }

    /**
     * spring cglib代理处理器
     */
    public abstract class CglibProxyHandler implements MethodInterceptor {

        public static CglibProxyHandler create() {
            return new CglibProxyHandler() {
            };
        }

        /**
         * Cglib默认代理
         *
         * @param proxy       proxy
         * @param method      method
         * @param args        args
         * @param methodProxy methodProxy
         * @return object
         * @throws Throwable exception
         */
        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            return methodProxy.invokeSuper(proxy, args);
        }
    }
}
