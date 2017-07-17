package com.glicerial.samples.cardataweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarController {

    @RequestMapping("/")
    public String cars() {
        return "index";
    }

    @RequestMapping("/addcar")
    public String addCar() {
        return "addcar";
    }
}
