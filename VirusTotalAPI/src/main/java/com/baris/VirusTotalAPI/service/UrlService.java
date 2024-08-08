package com.baris.VirusTotalAPI.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UrlService {

    private final OkHttpClient client;

    public UrlService(OkHttpClient client) {
        this.client = client;
    }
    @Value("${VirusTotalAPI.api.key}")
    private String apiKey;

  

    public String scanURL(String url) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "apikey="+apiKey+"&url="+url);
        System.out.println("BODY"+ body.toString());
        Request request = new Request.Builder()
                .url("https://www.virustotal.com/vtapi/v2/url/scan")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
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
    public String getVirusTotalReport(String url) throws IOException {
        String apiUrl = "https://www.virustotal.com/vtapi/v2/url/report?apikey=" + apiKey + "&resource=" + url + "&allinfo=false&scan=0";

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = null;
        try {
            //istek yapar ve yanıtı alır
            response = client.newCall(request).execute();
            //yanıt başarılıysa ve gövde boş değilse, body'i döndür
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } finally {
            //yanıt gövdesi varsa kapatır
            if (response != null && response.body() != null) {
                response.body().close();
            }

        }
    }
}


