package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.api.ClientAuditLogController;
import com.antzuhl.zeus.api.ServiceAliveController;
import com.antzuhl.zeus.config.ServerConfig;
import com.antzuhl.zeus.event.TaskApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ServiceAliveController.class, TaskApplicationListener.class,
        ClientAuditLogController.class})
@Component
public @interface ZeusProperty {
    String serverAddr(); // 管理端的ip地址

    String serverName() default "";

    String comment() default "";
}
