package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.server.NettyServer;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(NettyServer.class)
@Component
public @interface RpcService {
}