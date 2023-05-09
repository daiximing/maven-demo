package com.ddup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dai Ximing
 * @create 2023-05-09 11:00
 */
@RestController
public class TestPostman {

    @GetMapping("/testEnvironment")
    public void testEnvironment(){
        System.out.println("testEnvironment");
    }

    @PostMapping("/testEnvironment2")
    public String testEnvironment2(String name){
        return name;
    }
}
