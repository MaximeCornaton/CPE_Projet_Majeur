package com.pm.e8.FireManager.controller;

import com.project.model.dto.FireDto;
import org.springframework.web.bind.annotation.RestController;
import com.pm.e8.FireManager.service.FireService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RestController
public class FireCrt {
    private final FireService fServ;
    public FireCrt(FireService fServ) {
        this.fServ = fServ;
    }

    @GetMapping("/fires")
    public List<FireDto> getFires() {
        return fServ.getFireDtoList();
    }

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }



}
