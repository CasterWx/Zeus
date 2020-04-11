package com.demo.controller;

import com.antzuhl.zeus.client.RpcClient;
import com.antzuhl.zeus.entity.request.RpcRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value = "/hello")
    public String hello() throws InterruptedException {
        RpcClient rpcClient = RpcClient.getInstance();
        rpcClient.doConnect("localhost", 18868);
        Object object = rpcClient.send(
                new RpcRequest("1",
                        "com.demo.service.DoSomethingImpl",
                        "doHello", null, null));

        return object.toString();
    }

    @RequestMapping("/{id}")
    public String hello(@PathVariable Long id) throws SQLException {
        return id.toString();
    }
}
