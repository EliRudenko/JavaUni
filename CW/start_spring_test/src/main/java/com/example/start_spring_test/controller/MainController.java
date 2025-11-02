package com.example.start_spring_test.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    private final Map<String, String> users = new HashMap<>();

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (users.containsKey(username)) {
            model.addAttribute("error", "Пользователь уже существует");
            return "login";
        }
        users.put(username, password);
        model.addAttribute("message", "Регистрация успешна! Войдите.");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            session.setAttribute("user", username);
            return "redirect:/welcome";
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
    }

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        if (user == null) return "redirect:/";
        model.addAttribute("username", user);
        return "welcome";
    }
}
