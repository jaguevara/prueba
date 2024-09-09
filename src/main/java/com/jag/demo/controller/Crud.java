package com.jag.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class Crud {

    @GetMapping("/prueba")
    public ResponseEntity<?> getPrueba() {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("retorno", "Esto es una prueba");
        return new ResponseEntity<Map>(retorno, HttpStatus.OK);
    }
}
