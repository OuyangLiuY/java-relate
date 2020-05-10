package com;

import com.app.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

	public static void main(String[] args) {

	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();

		ac.register(AppConfig.class);
		ac.refresh();
	}
}
