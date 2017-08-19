package com.sedooe.actuator.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/actuator")
    public String getSomething() {
        return "index";
    }
}
