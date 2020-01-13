package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.ContextRefreshedListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
//@ComponentScan("com.antzuhl.zeus")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ContextRefreshedListener.class})
@Component
public @interface ZeusRegistry {
    String registryName() default "";
    String zkAddr() default "";
    String serverAddr() default "";
    String serverName() default "";
}