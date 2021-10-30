package com.bobocode.clients.nasaImagesClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bobocode.clients.nasaImagesClient.Pojos.*;

public class NasaImagesSocketClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final User ME = new User("Maryna", "Melnychuk");
    private static final SSLSocketFactory SOCKET_FACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();

    @SneakyThrows
    public static void main(String[] args) {
        sendRequestToBoboCode(getLargestPicture());
    }

    @SneakyThrows
    private static void sendRequestToBoboCode(Picture picture) {
        BoboCodeRequest request = new BoboCodeRequest(picture, ME);
        String body = MAPPER.writeValueAsString(request);
        URL bobocodeUrl = new URL("https://bobocode.herokuapp.com/nasa/pictures");
        try (var socket = (SSLSocket) SOCKET_FACTORY.createSocket(bobocodeUrl.getHost(), 443)) {
            var writer = new PrintWriter(socket.getOutputStream());
            writer.println(String.format("POST %s HTTP/1.1", bobocodeUrl.getPath()));
            writer.println(String.format("Host: %s", bobocodeUrl.getHost()));
            writer.println("Content-Type: application/json");
            writer.println(String.format("Content-Length : %s", body.length()));
            writer.println();
            writer.println(body);
            writer.flush();

            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader.lines().forEach(System.out::println);
        }
    }

    @SneakyThrows
    private static Picture getLargestPicture() {
        try (var socket = (SSLSocket) SOCKET_FACTORY.createSocket("api.nasa.gov", 443)) {
            var writer = new PrintWriter(socket.getOutputStream());
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.println("GET /mars-photos/api/v1/rovers/curiosity/photos?sol=12&api_key=DEMO_KEY HTTP/1.1");
            writer.println("Host: api.nasa.gov");
            writer.println();
            writer.flush();

            String jsonResponse = reader.lines().filter(line -> line.contains("{"))
                    .collect(Collectors.joining());
            NasaImages nasaImages = MAPPER.readValue(jsonResponse, NasaImages.class);
            var imageBySize = nasaImages.nasaImageInfos.stream()
                    .map(NasaImageInfo::getImgSrc)
                    .map(imageUrl -> Map.entry(imageUrl, getPhotoSize(imageUrl)))
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Can't find largest image!"));
            return new Picture(imageBySize.getKey(), imageBySize.getValue());
        }
    }

    @SneakyThrows
    private static Integer getPhotoSize(String url) {
        String locationUrl = getHeaderValue(new URL(url), "Location");
        String contentLength = getHeaderValue(new URL(locationUrl), "Content-Length");
        return Integer.valueOf(contentLength);
    }

    @SneakyThrows
    private static String getHeaderValue(URL url, String header) {
        try (var socket = (SSLSocket) SOCKET_FACTORY.createSocket(url.getHost(), 443)) {
            var writer = new PrintWriter(socket.getOutputStream());
            writer.println(String.format("HEAD %s HTTP/1.1", url.getPath()));
            writer.println(String.format("Host: %s", url.getHost()));
            writer.println();
            writer.flush();

            String formattedHeader = String.format("%s: ", header);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return reader.lines().filter(line -> line.startsWith(String.format(formattedHeader, header)))
                    .findFirst().orElseThrow().substring(formattedHeader.length());
        }
    }

    //TODO: INVESTIGATE WHY THIS METHOD DOES NOT WORK FOR ALL CASES
    /*@SneakyThrows
    private static Stream<String> sendRequest(URL url, String method, Set<String> headers, String body) {
        try (var socket = factory.createSocket(url.getHost(), 443)) {
            var writer = new PrintWriter(socket.getOutputStream());
            writer.println(String.format("%s %s HTTP/1.1", method, url.getPath()));
            writer.println("Host: " + url.getHost());
            headers.forEach(writer::println);
            writer.println();
            if (!Objects.isNull(body)) {
                writer.println(body);
            }
            writer.flush();
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader.lines().forEach(System.out::println);
            return reader.lines();
        }
    }*/
}
