package org.example.myservletapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Servlet")
public class SecondServlet extends HttpServlet {

    private static final long serialVersionUID = 1L; // вказує версію класу для серіалізації сервлету

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // метод doGet може бути залишений порожнім або використовуватися за потреби
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // форматування html
        response.setContentType("text/html");

        PrintWriter pw = response.getWriter();

        // отримуємо список параметрів з html-сторінки
        Enumeration<String> e = request.getParameterNames();

        // відображення параметрів та їх значень
        while (e.hasMoreElements()) {
            String pname = e.nextElement();
            pw.print(pname + " = ");
            String pvalue = request.getParameter(pname);
            pw.println(pvalue + "<br/>");
        }
        pw.println("<b>Усе вдалося!</b>");
        pw.close();
    }
}