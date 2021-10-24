package com.bobocode.clients;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageServletSocketClient {

    private static final String MESSAGE = "{\n" +
            "   \"name\":\"Maryna\",\n" +
            "   \"message\":\"Hey, what's up?\"\n" +
            "}";

    @SneakyThrows
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8081)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("POST /message HTTP/1.1");
            writer.println("Host: localhost");
            writer.println("Content-Type: application/json");
            writer.println(String.format("Content-Length: %d\n", MESSAGE.length()));
            writer.println(MESSAGE);
            writer.flush();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            reader.lines().forEach(System.out::println);
        }
    }
}
