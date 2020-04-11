package com.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserController {


    @RequestMapping("/{id}")
    public String hello(@PathVariable Long id) throws SQLException {
        return id.toString();
    }
}
