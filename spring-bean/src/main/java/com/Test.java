package com;

import com.app.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {

/*	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(AppConfig.class);
		ac.refresh();*/
		//ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");

	/*	AnnotationConfigApplicationContext ac =
				new AnnotationConfigApplicationContext();

		ac.register(AppConfig.class);
		ac.refresh();
		System.out.println(ac.getBean(InterClass.class).getInter());*/
		//byte short int long
		//float double
		//boolean
		//char
		//return address
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
		Object bean = context.getBean("&interClass");
		System.out.println(bean);
	}
}
