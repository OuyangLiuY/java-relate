package com.ywb.annotation;

import java.lang.annotation.*;


/**
 * @author chopsticks
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomResponseBody {
}