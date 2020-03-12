package com.ywb.annotation;


import java.lang.annotation.*;

/**
 * @author chopsticks
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomController {
    String value() default  "";
}
