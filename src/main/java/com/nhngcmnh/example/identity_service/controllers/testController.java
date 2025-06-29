package com.nhngcmnh.example.identity_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class testController {
    @GetMapping("/hello")
    String hello(){
        return "Hello sp3";
    }
}
