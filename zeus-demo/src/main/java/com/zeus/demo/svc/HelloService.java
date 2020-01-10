package com.zeus.demo.svc;


import com.antzuhl.zeus.client.RpcClient;
import com.antzuhl.zeus.entity.request.RpcRequest;
import com.antzuhl.zeus.enums.StatusEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {

    @RequestMapping(value = "/hello")
    public String hello() throws InterruptedException {
        RpcClient rpcClient = new RpcClient();
        rpcClient.doConnect("localhost", 18868);
        Object object = rpcClient.send(
                new RpcRequest("1", StatusEnum.REMOTE_REGISTRY_MSG.code(),
                        "com.antzuhl.zeusdemo2.service.impl.DoSomethingImpl",
                        "doHello", null, null));

        return object.toString();
    }
}
