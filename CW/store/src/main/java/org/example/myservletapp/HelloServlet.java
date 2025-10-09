package org.example.myservletapp;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;






import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/* за часів давніх богів, воєвод та царів...
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
*/

// https://javarush.com/quests/lectures/questservlets.level11.lecture07
// @WebServlet — це анотація, що використовується для визначення сервлета в Java веб-додатках.
// вона вказує URL-патерн, за яким буде доступний сервлет.
// замість використання файлу web.xml для реєстрації сервлета, @WebServlet дозволяє налаштовувати сервлет безпосередньо в коді.
// анотація підтримує додаткові параметри, такі як ім'я сервлета та параметри ініціалізації, для більш детального налаштування.
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // отримання числа з параметра запиту
        String numberParam = request.getParameter("number");
        int number = 0;

        // спроба перетворити параметр у число
        try {
            number = Integer.parseInt(numberParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "неправильний числовий формат");
            return;
        }

        // збільшення числа на 1
        int result = number + 1;

        // надсилання результату у відповідь
        response.setContentType("text/plain");
        response.getWriter().write("Ось уже й результати: " + result);

        // приклад надсилання HTML-сторінки у відповідь
        /*response.setContentType("text/html");
        PrintWriter printWriter  = response.getWriter();
        printWriter.println("<h1>- Привіт, Сервер!</h1>");
        printWriter.println("<h1>- Вітаю, Алекс!</h1>");*/
    }
}




/*
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}

*/




