package com.antzuhl.zeus.controller.impl;

import com.antzuhl.zeus.annotation.RpcService;
import com.antzuhl.zeus.controller.DoSomething;

@RpcService
public class DoSomethingImpl implements DoSomething {
    @Override
    public void doHello() {
        System.out.println("hello,world");
    }
}
