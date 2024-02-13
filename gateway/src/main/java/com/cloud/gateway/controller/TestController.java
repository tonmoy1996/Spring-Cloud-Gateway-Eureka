package com.cloud.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Value("${string.test:hi}")
    public String text;
    @GetMapping(value = "/hello")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok(text);
    }
}
