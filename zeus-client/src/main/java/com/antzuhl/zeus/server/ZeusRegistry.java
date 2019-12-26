package com.antzuhl.zeus.server;

import com.antzuhl.zeus.registry.ServiceRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceRegistry.class)
@Component
public @interface ZeusRegistry {
    String registryName() default "";
    String ipAddress() default "";
}