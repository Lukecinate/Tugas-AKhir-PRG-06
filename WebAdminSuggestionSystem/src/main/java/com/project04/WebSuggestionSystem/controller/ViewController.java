package com.project04.WebSuggestionSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping("/login")
    public String index(){
        return "loginSS";
    }

    @GetMapping("/home")
    public String home(){
        return "index";
    }
}
