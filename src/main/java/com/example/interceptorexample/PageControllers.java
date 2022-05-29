package com.example.interceptorexample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageControllers {

    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}
