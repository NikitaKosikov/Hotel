package com.kosnik.service.scheduler;

import com.kosnik.service.CompetitorService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SearchCompetitors {
    @Value("${api.geoapify.url}")
    private String url;
    @Value("${api.geoapify.api-key}")
    private String apiKey;

    @Value("${api.geoapify.categories-search}")
    private String categoriesSearch;
    @Value("${api.geoapify.filter-coordinates}")
    private String filterCoordinates;
    private final CompetitorService competitorService;
    @PostConstruct
    public void setUp() throws IOException {
        geocodeRequest("11 Wall St, New York, NY 10005");
    }

    //@Scheduled(cron = "${here.geocoder.retry}")
    public void geocodeRequest(String query) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"?categories="+categoriesSearch+"&filter="+filterCoordinates+"&apiKey="+apiKey)
                //.url("https://api.geoapify.com/v2/places?categories=accommodation.hostel&filter=circle:27.567444,53.893009,9000&apiKey=b1164bc5329244f7a69215ca1fe381f4")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        competitorService.loadCompetitor(body);
    }
}
