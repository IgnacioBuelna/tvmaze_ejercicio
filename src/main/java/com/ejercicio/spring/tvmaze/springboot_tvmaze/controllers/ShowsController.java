package com.ejercicio.spring.tvmaze.springboot_tvmaze.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.ObjectMapper;


@RestController
public class ShowsController {

    @GetMapping("/info")
    public String info() {
        return "Hola mundo";
    }

    @GetMapping("/search")
    public List<ShowResponse> getSearchShow(@RequestParam String search_query) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tvmaze.com/search/shows?q=girls"))
                .GET()
                .header("Accept", "application/json")
                .build();
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            List<ShowMapper> showMappers = mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, ShowMapper.class));

            List<ShowResponse> showResponses = new ArrayList<ShowResponse>();
            for (ShowMapper showMapper : showMappers) {
                ShowResponse showResponse = new ShowResponse();
                showResponse.id = showMapper.show.id;
                showResponse.name = showMapper.show.name;
                showResponse.channel = "";
                showResponse.summary = showMapper.show.summary;
                showResponse.genres = showMapper.show.genres;

                if (showMapper.show.network != null) {
                    showResponse.channel = showMapper.show.network.name;
                }
                if (showMapper.show.webChannel != null) {
                    showResponse.channel = showMapper.show.webChannel.name;
                }
                showResponses.add(showResponse);
            }
            return showResponses;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
}
