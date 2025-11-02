package com.example.asept_start_test;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
class AseptStartTestApplication
{
    public static void main(String[] args) { SpringApplication.run(AseptStartTestApplication.class, args); }
}

@Aspect
@Component
class LoggingAspect
{

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Around("execution(* com.example.asept_start_test.HelloController.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable
    {
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime start = LocalDateTime.now();
        long startTime = System.currentTimeMillis();

        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("\nПочаток виконання методу: " + methodName);
        System.out.println("1. Час старту: " + start.format(FORMATTER));

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            LocalDateTime end = LocalDateTime.now();
            long duration = endTime - startTime;
            System.out.println("2. Час завершення: " + end.format(FORMATTER));
            System.out.println("3. Загальний час виконання: " + duration + " мс");
            System.out.println("\nЗавершено метод: " + methodName);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
        }

        return result;
    }
}

@RestController
class HelloController
{
    @GetMapping("/")
    public String sayHello()
    {
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return "Hello Spring with AOP and timing!";
    }

    @GetMapping("/slow")
    public String slowMethod()
    {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        return "This was a slow method!";
    }
}

@Component
class BrowserLauncher
{
    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser()
    {
        System.setProperty("java.awt.headless", "false");
        var desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI("http://localhost:8080"));
        } catch (IOException | URISyntaxException e) { }
    }
}
