package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.aop.FlowMonitorAcpect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({FlowMonitorAcpect.class})
@Documented
public @interface FlowMonitor {
    String name() default "";
}