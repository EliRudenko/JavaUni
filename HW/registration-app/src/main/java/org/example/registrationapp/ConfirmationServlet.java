package org.example.registrationapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ConfirmationServlet")
public class ConfirmationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String name = session != null ? (String) session.getAttribute("name") : "Гость";
        String email = session != null ? (String) session.getAttribute("email") : "";

        // Отправляем данные в JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"name\":\"" + name + "\", \"email\":\"" + email + "\"}");
        out.close();
    }
}
