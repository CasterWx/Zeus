package com.zeus.demo.svc;


import com.antzuhl.zeus.server.FlowMonitor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {

    @RequestMapping(value = "/hello")
    @FlowMonitor(name = "hello")
    public String hello() {
        return "Hello! " ;
    }
}
