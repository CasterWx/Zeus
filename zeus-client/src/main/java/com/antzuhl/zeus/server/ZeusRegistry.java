package com.antzuhl.zeus.server;

import com.antzuhl.zeus.registry.ServiceRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@ComponentScan("com.antzuhl.zeus")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceRegistry.class)
@Component
public @interface ZeusRegistry {
    String registryName() default "";
    String zkAddr() default "";
    String serverAddr() default "";
    String serverName() default "";
}