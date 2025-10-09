package org.example.registrationapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Получаем данные из формы
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Сохраняем данные в сессии
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
        session.setAttribute("email", email);

        // Перенаправляем на страницу подтверждения
        response.sendRedirect("confirmation.html");
    }
}
