package com.bobocode.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    private final String NAME = "name";

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        Optional<String> requestName = Optional.ofNullable(req.getParameter(NAME));
        Optional<String> sessionName = Optional.ofNullable(
                (String)session.getAttribute(NAME));
        requestName.filter(nameValue -> sessionName.isEmpty())
                .ifPresent((nameValue) -> session.setAttribute(NAME, nameValue));
        String name = requestName.or(() -> sessionName).orElse("default");
        String eveningExpresion = String.format("Good evening, %s", name);
        resp.getWriter().print(eveningExpresion);
    }
}
