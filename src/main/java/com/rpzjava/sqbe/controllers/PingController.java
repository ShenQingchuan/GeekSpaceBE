package com.rpzjava.sqbe.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/")
    public String ping_pong() {
        return "HELLO SICNU Qualification BE API.";
    }
}
