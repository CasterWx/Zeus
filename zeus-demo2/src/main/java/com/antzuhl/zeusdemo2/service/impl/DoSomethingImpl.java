package com.antzuhl.zeusdemo2.service.impl;

import com.antzuhl.zeus.annotation.RpcService;

@RpcService
public class DoSomethingImpl implements DoSomething {
    @Override
    public void doHello() {
        System.out.println("hello,world");
    }
}
