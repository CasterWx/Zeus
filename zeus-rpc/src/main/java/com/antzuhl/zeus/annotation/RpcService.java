package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.server.NettyServer;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(NettyServer.class)
@Component
public @interface RpcService {
}