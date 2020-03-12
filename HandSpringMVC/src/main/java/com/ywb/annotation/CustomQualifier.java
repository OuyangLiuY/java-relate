package com.ywb.annotation;


import java.lang.annotation.*;

/**
 * @author chopsticks
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomQualifier {
    String value() default "";
}
