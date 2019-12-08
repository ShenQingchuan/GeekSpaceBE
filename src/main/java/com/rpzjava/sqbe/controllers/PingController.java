package com.rpzjava.sqbe.controllers;

import com.rpzjava.sqbe.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/")
    public Object helloGeekSpace() {
        return ResultUtils.success("Hello Geek Space API !");
    }
}
