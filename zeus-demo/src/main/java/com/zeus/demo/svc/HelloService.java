package com.zeus.demo.svc;

import com.antzuhl.zeus.server.RpcService;

@RpcService(HelloService.class)
public class HelloService {
    public String hello(String name) {
        return "Hello! " + name;
    }
}
