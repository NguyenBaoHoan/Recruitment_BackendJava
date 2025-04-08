package com.example.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloWorld {
    @GetMapping
    public String HelloWorld() {
        return "Hello World";
    }

}
