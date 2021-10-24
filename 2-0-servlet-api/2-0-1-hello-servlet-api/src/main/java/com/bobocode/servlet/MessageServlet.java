package com.bobocode.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Message message = MAPPER.readValue(req.getReader(), Message.class);
        System.out.println(message);
    }

    @ToString
    @Getter
    static class Message {
        private String name;
        private String message;
    }
}
