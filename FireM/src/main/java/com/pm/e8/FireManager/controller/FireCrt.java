package com.pm.e8.FireManager.controller;

import com.pm.e8.FireManager.model.Fire;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.pm.e8.FireManager.service.FireService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;


@RestController
public class FireCrt {
    private final FireService fServ;
    public FireCrt(FireService fServ) {
        this.fServ = fServ;
    }

    @GetMapping("/fires")
    public List<Fire> getFires() {
        return fServ.getFireList();
    }
    @GetMapping("/fires/{idf}")
    public Fire getFireById(@PathVariable int idf){
        return fServ.GetFireById(idf);
    }

    @GetMapping("/fireCoord/{idf}")
    public Coord getFireCoord(@PathVariable int idf){
        return fServ.GetFireCoord(idf);
    }

    @GetMapping("/fireAround/{idv}/{radius}")
    public List<Fire> getFireAround(@PathVariable int idv, @PathVariable int radius){
        return fServ.getFiresAround(idv, radius);
    }

    @GetMapping("/fire/{type}")
    public List<FireDto> getFireType(@PathVariable String type){
        return fServ.getTypeFires(type);
    }

    @GetMapping("/types")
    public List<String> getTypes(){
        return Collections.singletonList(fServ.getTypes());
    }


}
