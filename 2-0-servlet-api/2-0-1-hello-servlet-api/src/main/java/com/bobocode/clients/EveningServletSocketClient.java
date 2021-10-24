package com.bobocode.clients;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EveningServletSocketClient {
    @SneakyThrows
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8081)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("GET /evening HTTP/1.1\n"
                    + "Host: localhost\n"
                    + "Cookie: JSESSIONID=C078A966A96AC4C21C5E98FE1097ED79\n");
            writer.flush();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            reader.lines().forEach(System.out::println);
        }
    }
}