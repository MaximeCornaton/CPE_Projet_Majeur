package com.pm.e8.FireManager.controller;

import com.pm.e8.FireManager.model.Fire;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/fires/{idf}")
    public Fire getFireById(@PathVariable int idf){
        return fServ.GetFireById(idf);
    }

    @GetMapping("/fireCoord/{idf}")
    public Coord getFireCoord(@PathVariable int idf){
        return fServ.GetFireCoord(idf);
    }

    @GetMapping("/fireAround/{idf}/{radius}")
    public List<Fire> getFireAround(@PathVariable int idf, @PathVariable int radius){
        return fServ.getFiresAround(idf, radius);
    }



}
