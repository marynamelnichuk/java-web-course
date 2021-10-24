package com.bobocode.clients;

import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EveningServletHttpClient {

    private final static String EVENING_GET_ENDPOINT = "http://localhost:8081/evening?name=%s";

    @SneakyThrows
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(EVENING_GET_ENDPOINT, "Maryna")))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
