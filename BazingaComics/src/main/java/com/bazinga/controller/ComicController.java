package com.bazinga.controller;

import com.bazinga.dao.ComicDAO;
import com.bazinga.model.Comic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalog")
public class ComicController extends HttpServlet {
    private ComicDAO comicDAO = new ComicDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Comic> comics = comicDAO.getAllComics();
        request.setAttribute("comics", comics);
        request.getRequestDispatcher("/catalog.jsp").forward(request, response);
    }
}
