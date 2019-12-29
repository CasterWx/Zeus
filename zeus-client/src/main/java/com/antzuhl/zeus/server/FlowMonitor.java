package com.antzuhl.zeus.server;

import com.antzuhl.zeus.aop.FlowMonitorAcpect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowMonitor {
    String name() default "";
}