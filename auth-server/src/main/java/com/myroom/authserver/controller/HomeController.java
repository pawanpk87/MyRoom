package com.myroom.authserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/home")
public class HomeController {
    @GetMapping
    public String home(){
        System.out.println("Got the request");
        return "Auth Home";
    }
}
