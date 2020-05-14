package com.spring.principle;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SpringBegin {

    @Test
    public void springTheory() throws Exception {
        SpringController springController = new SpringController();
        Class<? extends SpringController> clazz = springController.getClass();
        SpringService springService = new SpringService();
        Field springServiceField = clazz.getDeclaredField("springService");
        springServiceField.setAccessible(true);
        springServiceField.set(springController, springService);
        springController.print();
    }

    @Test
    public void springSetDITheory() throws Exception {
        SpringController springController = new SpringController();
        Class<? extends SpringController> clazz = springController.getClass();
        //获取共有的 -> public
        //Returns an array containing {@code Field} objects reflecting all
        // the accessible public fields of the class or interface represented by
        // this {@code Class} object.
        clazz.getFields();
        //获取所有的
        //包括 public的protected ，default，and private ， 不包括inherited fields.(父类)
        //Returns an array of {@code Field} objects reflecting all the fields
        //declared by the class or interface represented by this
        //{@code Class} object. This includes public, protected, default
        // (package) access, and private fields, but excludes inherited fields.
        clazz.getDeclaredFields();

        SpringService springService = new SpringService();

        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(System.out::println);

        Field springServiceField = clazz.getDeclaredField("springService");
        springServiceField.setAccessible(true);
        String name = springServiceField.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        System.out.println("get" + name);
        String setMethodName = "set" + name;
        Method method = clazz.getMethod(setMethodName, SpringService.class);
        method.invoke(springController, springService);
        springController.print();
    }

    @Test
    public void springAnnotationDITheory() throws Exception {
        SpringController springController = new SpringController();
        Class<? extends SpringController> clazz = springController.getClass();
        Field springServiceField = clazz.getDeclaredField("springService");
        springServiceField.setAccessible(true);
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            System.out.println(field.getName());
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation != null) {
                Class<?> type = field.getType();
                try {
                    Object instance = type.getConstructor().newInstance();
                    springServiceField.set(springController, instance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        springController.print();
    }
}
