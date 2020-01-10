package com.antzuhl.zeusdemo2.service.impl;

import com.antzuhl.zeus.annotation.RpcService;

import java.util.ArrayList;
import java.util.List;

@RpcService
public class DoSomethingImpl implements DoSomething {
    private static List<String> users = new ArrayList<>();

    @Override
    public List<String> doHello() {
        users.add("user-1");
        users.add("user-2");
        users.add("user-3");
        return users;
    }
}
