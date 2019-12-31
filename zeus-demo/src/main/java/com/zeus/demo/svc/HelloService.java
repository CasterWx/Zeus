package com.zeus.demo.svc;


import com.antzuhl.zeus.client.RpcClient;
import com.antzuhl.zeus.entity.request.RpcRequest;
import com.antzuhl.zeus.server.FlowMonitor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {

    @RequestMapping(value = "/hello")
    public String hello() {
        RpcClient rpcClient = new RpcClient();
        try {
            rpcClient.doConnect("localhost", 18868);
            rpcClient.send(new RpcRequest("1", "com.antzuhl.zeusdemo2.service.impl.DoSomethingImpl", "doHello", null, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello! " ;
    }
}
