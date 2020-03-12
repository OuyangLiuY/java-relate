package com.ywb.agrumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface ArgumentResolver
{
    /**
     * 判断当前的类是继承于ArgumentResolver
     * @param type 当前参数注解的类对象
     * @param paramIndex 参数下标
     * @param method 当前的方法
     * @return
     */
    public boolean support(Class<?> type, int paramIndex, Method method);

    /**
     *
     * @param request
     * @param response
     * @param type
     * @param paramIndex
     * @param method
     * @return
     */
    public Object argumentResolver(HttpServletRequest request,
                                   HttpServletResponse response, Class<?> type,
                                   int paramIndex,
                                   Method method);

}
