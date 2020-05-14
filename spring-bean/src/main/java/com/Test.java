package com;

import com.app.*;
import com.service.BeanService;
import com.service.IndexService;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {

/*	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(AppConfig.class);
		ac.refresh();*/
		//ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");

		AnnotationConfigApplicationContext ac =
				new AnnotationConfigApplicationContext();

		ac.register(AppConfig.class);
		ac.refresh();
		System.out.println(ac.getBean(InterClass.class).getInter());
		//byte short int long
		//float double
		//boolean
		//char
		//return address
/*		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
		Object bean = context.getBean("&interClass");
		System.out.println(bean);*/

		GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
		genericBeanDefinition.setBeanClass(IndexService.class);
		genericBeanDefinition.setScope("single");

		RootBeanDefinition rootBeanDefinition =new RootBeanDefinition();
		rootBeanDefinition.getPropertyValues().add("name","test");
		rootBeanDefinition.setBeanClass(BeanService.class);

		ac.registerBeanDefinition("beanName",rootBeanDefinition);
	}
}
