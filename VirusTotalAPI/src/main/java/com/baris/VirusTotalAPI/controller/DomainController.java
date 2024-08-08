package com.baris.VirusTotalAPI.controller;

import com.baris.VirusTotalAPI.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DomainController {
    @Autowired
    private DomainService service;
//http://localhost:8080/domainReport?domainName=facebook.com
    @GetMapping("/domainReport")
    public String domainReport(@RequestParam String domainName)
    {
        try {
            return service.scanDomain(domainName);
        }
         catch (IOException e) {
             e.printStackTrace();
             return "Error"+e.getMessage();
        }
    }
    @GetMapping("/scanIP")
    public String scanIP(@RequestParam String ipAdress)
    {
        try {
            return service.scanIPAdress(ipAdress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
