package com.example.themecookiedemo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ThemeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String theme = request.getParameter("theme");

        if (theme != null && (theme.equals("dark") || theme.equals("light"))) {
            Cookie themeCookie = new Cookie("theme", theme);
            themeCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(themeCookie);
        }

        response.sendRedirect("index.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String theme = "light";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("theme".equals(cookie.getName())) {
                    theme = cookie.getValue();
                    break;
                }
            }
        }

        response.setContentType("text/plain");
        response.getWriter().write(theme);
    }
}
