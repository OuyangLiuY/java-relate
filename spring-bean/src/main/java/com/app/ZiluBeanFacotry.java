package com.app;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

//后置处理器
@Component
public class ZiluBeanFacotry implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        GenericBeanDefinition a = (GenericBeanDefinition) beanFactory.getBeanDefinition("interClass");
        System.out.println("a mode = " + a.getAutowireMode());
    }
}
