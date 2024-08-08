package com.baris.VirusTotalAPI.controller;

import com.baris.VirusTotalAPI.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UrlCheckController {
    @Autowired
    private UrlService urlService;
//    http://localhost:8080/urlReport?url=https://www.example.com
    @GetMapping("/urlReport")
        public String getUrlReport(@RequestParam String url) {
            try {
                return urlService.getVirusTotalReport(url);
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }
//    @PostMapping("/scan")
//    public String scanUrl(@RequestParam String url)
//    {
//        try {
//            return urlService.scanURL(url);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @PostMapping("/scan")
    public String scanUrl2(@RequestParam String url) throws IOException {
        try {
            return urlService.scanURL(url);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

