package com.bazinga.controller;

import com.bazinga.dao.ComicDAO;
import com.bazinga.model.Cart;
import com.bazinga.model.CartItem;
import com.bazinga.model.Comic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cart")
public class CartController extends HttpServlet {
    private ComicDAO comicDAO = new ComicDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("comicId");
        String qty = request.getParameter("quantity");
        Long comicId = Long.parseLong(id);
        int quantity = 1;
        try { quantity = Integer.parseInt(qty); } catch (Exception ignored) {}

        Comic comic = comicDAO.findById(comicId);
        if (comic == null) {
            response.sendRedirect(request.getContextPath() + "/catalog");
            return;
        }

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) { cart = new Cart(); session.setAttribute("cart", cart); }
        cart.addItem(new CartItem(comic, quantity));
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
}
