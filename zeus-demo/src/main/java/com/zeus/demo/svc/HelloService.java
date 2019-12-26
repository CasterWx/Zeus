package com.zeus.demo.svc;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {
    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello! " ;
    }
}
