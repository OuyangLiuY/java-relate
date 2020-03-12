package com.ywb.annotation;


import java.lang.annotation.*;

/**
 * @author chopsticks
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRequestParam {
    String value() default "";
}
