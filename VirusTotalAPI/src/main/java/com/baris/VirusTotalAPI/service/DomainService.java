package com.baris.VirusTotalAPI.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DomainService {

    @Value("${VirusTotalAPI.api.key}")
    private String apiKey;

    private final OkHttpClient client;

    public DomainService(OkHttpClient client) {
        this.client = client;
    }
    //http://localhost:8080/scanIP?ipAdress=8.8.8.8
    public String scanIPAdress(String ipAdress) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.virustotal.com/vtapi/v2/ip-address/report?apikey="+apiKey+"&ip="+ipAdress)
                .get()
                .addHeader("accept", "application/json")
                .build();

       // Response response = client.newCall(request).execute();
        try(Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body()!=null)
            {
                return response.body().string();
            }
            else
            {
                throw new IOException("Unexpected code"+response);
            }
        }
    }
    public String scanDomain(String domainName) throws IOException {

        Request request = new Request.Builder()
                .url("https://www.virustotal.com/vtapi/v2/domain/report?apikey="+apiKey + "&domain="+ domainName)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body()!=null)
            {
                return response.body().string();
            }
            else
            {
                throw new IOException("Unexpected code"+response);
            }
        }
    }
    }

