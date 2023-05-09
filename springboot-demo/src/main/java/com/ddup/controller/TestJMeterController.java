package com.ddup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dai Ximing
 * @create 2023-05-09 10:52
 */
@RestController
public class TestJMeterController {
    @GetMapping("/testJMeter")
    public void testJMeter(){
        System.out.println("hello jmeter");
    }
}
