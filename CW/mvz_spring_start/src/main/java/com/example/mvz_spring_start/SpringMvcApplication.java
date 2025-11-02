package com.example.mvz_spring_start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);
    }

}

// автоматичне відкриття браузера після запуску додатку
@Component
class BrowserLauncher {
    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        System.setProperty("java.awt.headless", "false"); // вимикаємо headless-режим
        var desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:8080/contacts")); // відкриваємо браузер
        } catch (IOException | URISyntaxException e) {
            // ігноруємо помилки, якщо браузер не вдалося відкрити
        }
    }
}