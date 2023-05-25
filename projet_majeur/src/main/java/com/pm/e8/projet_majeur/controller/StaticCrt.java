package com.pm.e8.projet_majeur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticCrt {

    @GetMapping({"/", "/index"})
    public String index(){
        return "index.html";
    }
}
