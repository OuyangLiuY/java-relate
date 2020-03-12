package com.ywb.service;

import com.ywb.agrumentResolver.ArgumentResolver;
import com.ywb.annotation.CustomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chopsticks
 */
@CustomService("CustomHandlerAdapter")
public class CustomHandlerAdapter implements HandlerAdapterService {
    @Override
    public Object[] handle(HttpServletRequest req, HttpServletResponse resp, Method method, Map<String, Object> beans)
    {
        //获取方法中含义的参数
        Class<?>[] paramClazzs = method.getParameterTypes();
        System.out.println("======当前需要解析的参数对应的类=========");
        for(Class<?> clazz: paramClazzs) {
            System.out.println(clazz);
        }

        // 定义一个返回参数的结果集
        Object[] args = new Object[paramClazzs.length];
//		Object[] args = {req, resp, "chenyanwu", "20"};

        // 定义一个ArgumentResolver实现类的Map
        Map<String, Object> argumentResolvers = getBeansOfType(beans, ArgumentResolver.class);

        System.out.println("======当前需要解析的参数对应的类实例化=========");
        for(Map.Entry<String, Object> map: argumentResolvers.entrySet()) {
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }

        //定义参数索引
        int paramIndex = 0;
        //定义数组下标索引
        int i = 0;
        // 开始处理参数
        for(Class<?> paramClazz: paramClazzs) {
            //哪个参数对应了哪个参数解析类,用策略模式来找
            for (Map.Entry<String, Object> entry : argumentResolvers.entrySet()) {
                ArgumentResolver ar = (ArgumentResolver)entry.getValue();

                if (ar.support(paramClazz, paramIndex, method)) {
                    args[i++] = ar.argumentResolver(req, resp, paramClazz, paramIndex,method);
                }
            }
            paramIndex++;
        }

        return args;

       // return new Object[0];
    }
    /**
     * @param beans IOC容器中全部的bean
     * @param intfType 定义的ArgumentResolver类
     * @return
     */
    private Map<String, Object> getBeansOfType(Map<String, Object> beans,
                                               Class<ArgumentResolver> intfType) {
        Map<String, Object> resultBeans = new HashMap<>();
        for(Map.Entry<String, Object> map: beans.entrySet()) {
            // 获取满足ArgumentResolver接口的bean
            Class<?>[] intfs = map.getValue().getClass().getInterfaces();

            if(intfs != null && intfs.length >0) {
                for(Class<?> intf: intfs) {
                    // 将满足的bean存储在resultBeans中
                    if(intf.isAssignableFrom(intfType)) {
                        resultBeans.put(map.getKey(), map.getValue());
                    }
                }
            }
        }
        return resultBeans;
    }

}
