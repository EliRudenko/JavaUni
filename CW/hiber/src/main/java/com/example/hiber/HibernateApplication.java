package com.example.hiber;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// запуск додатку - http://localhost:8080/
// має бути піднятий MySQL
@SpringBootApplication
public class HibernateApplication {
	public static void main(String[] args) {
		SpringApplication.run(HibernateApplication.class, args);
	}
}

@Component
class BrowserLauncher {
    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        System.setProperty("java.awt.headless", "false"); // вимикаємо headless-режим для роботи з Desktop
		// headless-режим у Java (властивість java.awt.headless=true) - це режим, коли JVM працює без графічного інтерфейсу (GUI),
		// що корисно для серверів чи контейнерів (наприклад, Docker), де не потрібні вікна, миші чи екрани.
		// він вимикається (false), бо клас Desktop (для відкриття браузера чи файлів) потребує доступу до GUI-компонентів.
		// без цього код не спрацює на системах з графікою, як наприклад локальна машина з Eclipse.
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:8080")); // відкриваємо браузер
        } catch (Exception e) {
            // ігноруємо помилки, якщо браузер не вдалося відкрити
        }
    }
} 
