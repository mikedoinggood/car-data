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

    @RequestMapping("/cars/{id}")
    public String carDetail() {
        return "cardetail";
    }

    @RequestMapping("/cars/{id}/edit")
    public String editDetail() {
        return "editcar";
    }

    @RequestMapping("/charts")
    public String charts() {
        return "charts";
    }
}
