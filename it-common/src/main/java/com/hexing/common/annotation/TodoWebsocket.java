package com.hexing.common.annotation;

import java.lang.annotation.*;


@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TodoWebsocket {

    public String type() default "Bill";

}
