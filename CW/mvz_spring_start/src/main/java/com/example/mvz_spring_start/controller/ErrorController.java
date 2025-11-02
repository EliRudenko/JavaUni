package com.example.mvz_spring_start.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
