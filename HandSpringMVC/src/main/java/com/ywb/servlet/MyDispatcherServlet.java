package com.ywb.servlet;


import com.ywb.annotation.CustomController;
import com.ywb.annotation.CustomQualifier;
import com.ywb.annotation.CustomRequestMapping;
import com.ywb.annotation.CustomService;
import com.ywb.controller.MyController;
import com.ywb.service.HandlerAdapterService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDispatcherServlet extends HttpServlet
{
    //当前加载的所有类
    private List <String> classNames = new ArrayList<>();
    //IOC容器的Map
    private Map<String,Object> beans = new HashMap<>();
    // 储存路径和方法的映射关系
    private Map<String,Object> handlerMap = new HashMap<>();
    public MyDispatcherServlet()
    {
        super();
        System.out.println("MyDispatcherServlet().....");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet()....");
      this.doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("doPost()....");
        // 通过req获取请求的uri /HandSpringMVC/custom/query
        String uri = req.getRequestURI();

        // /HandSpringMVC
        String context = req.getContextPath();
        System.out.println("context:" + context);
        String path = uri.replaceAll(context, "");
        System.out.println("[path]:" + context);
        // 通过当前的path获取handlerMap的方法名
        Method method = (Method) handlerMap.get(path);
        // 获取beans容器中的bean
        MyController instance = (MyController) beans.get("/" + path.split("/")[1]);

        // 处理参数
        HandlerAdapterService ha = (HandlerAdapterService) beans.get("CustomHandlerAdapter");
        Object[] args = ha.handle(req, resp, method, beans);

        // 通过反射来实现方法的调用

        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("init()....");

        //1.扫描需要的实例化的类
        doScanPackage("com.ywb");
        System.out.println("当前 文件下所有的class类.....");
        for(String name : classNames)
        {
            System.out.println(name);
        }
        //2.实例化
        doInstance();
        System.out.println("当前实例化的对象信息...");
        for(Map.Entry<String,Object> map:beans.entrySet())
        {
            System.out.println("key:"+map.getKey()+",value:"+map.getValue());
        }
        //3. 将IOC容器中的service对象设置给controller层定义的field上
        doIoc();
        //4.建立path与method的映射关系
        handlerMapping();
        System.out.println("Controller层的path和方法映射....");
        for(Map.Entry<String ,Object> map : handlerMap.entrySet())
        {
            System.out.println("key:" + map.getKey()+",value="+map.getValue());
        }

    }
    private void doScanPackage(String basePackage) {
        String replaceAll = basePackage.replaceAll("\\.", "/");

        URL resource = this.getClass().getClassLoader().getResource("/" + replaceAll);
        assert resource != null;
        String file = resource.getFile();
        File files = new File(file);
        String[] strings = files.list();
        assert strings != null;
        for(String path : strings)
        {
            File filePath = new File(file  + path);
            //如果是目录就要递归
            if(filePath.isDirectory())
            {
                doScanPackage(basePackage+'.'+path);
            }else{
                classNames.add(basePackage+'.'+filePath.getName());
            }
        }
    }
    private void doInstance() {
        if(classNames.isEmpty())
        {
            System.out.println("doScanner Failed.....");
        }
        //开始实例化
        for(String className : classNames)
        {
            String cn = className.replaceAll(".class","");
            try {
                Class<?> clazz = Class.forName(cn);
                if(clazz.isAnnotationPresent(CustomController.class))
                {
                    CustomRequestMapping requestMapping = clazz.getAnnotation(CustomRequestMapping.class);
                    String key = requestMapping.value();
                    //beans的value为实例化对象
                    Object value = clazz.newInstance();
                    beans.put(key,value);
                }else if(clazz.isAnnotationPresent(CustomService.class))
                {
                    CustomService service = clazz.getAnnotation(CustomService.class);
                    String key = service.value();
                    Object value = clazz.newInstance();
                    beans.put(key,value);
                }else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
    private void doIoc() {
        if(beans.isEmpty()){
            System.out.println("no class is instance...");
            return;
        }
        for(Map.Entry<String,Object> entry : beans.entrySet() )
        {
            //获取实例
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if(clazz.isAnnotationPresent(CustomController.class)){
                Field[] declaredField = clazz.getDeclaredFields();
                for(Field field : declaredField){
                    //如果当前的成员变量使用注解customRequestMapping进行处理
                    if(field.isAnnotationPresent(CustomQualifier.class)){
                        CustomQualifier qualifier = field.getAnnotation(CustomQualifier.class);
                        String value = qualifier.value();

                        //此类成员便俩设在为private，需要强制设置
                        field.setAccessible(true);
                        try {
                            field.set(instance,beans.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }
    }
    private void handlerMapping() {
        if(beans.isEmpty()){
            System.out.println("no class is instance......");
            return;
        }
        for(Map.Entry<String, Object> map: beans.entrySet()) {
            // 获取当前的对象
            Object instance = map.getValue();

            // 获取当前的类
            Class<?> clazz = instance.getClass();
            // 获取注解当前为Controller的类
            if(clazz.isAnnotationPresent(CustomController.class)) {
                // 获取类上的路径
                CustomRequestMapping clazzRm = clazz.getAnnotation(CustomRequestMapping.class);
                String clazzPath = clazzRm.value();

                // 处理方法
                Method[] methods = clazz.getMethods();
                for(Method method: methods) {
                    // 判断注解为RequestMapping
                    if(method.isAnnotationPresent(CustomRequestMapping.class)) {
                        // 获取方法上的路径
                        CustomRequestMapping methodRm = method.getAnnotation(CustomRequestMapping.class);
                        String methodPath = methodRm.value();

                        // 将类上的路径+方法上的路径 设置为key，方法设置为value
                        handlerMap.put(clazzPath + methodPath, method);
                    }
                }
            }
        }
    }





    @Override
    public void log(String msg) {
        super.log(msg);
    }

    @Override
    public void log(String message, Throwable t) {
        super.log(message, t);
    }
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

}
