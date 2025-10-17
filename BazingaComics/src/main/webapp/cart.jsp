<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.bazinga.model.Cart, com.bazinga.model.CartItem" %>
<html>
<head>
    <title>Кошик</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<h2>Ваш кошик</h2>
<%
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null || cart.getItems().isEmpty()) {
%>
<p>Кошик порожній. <a href="catalog">Повернутися до каталогу</a></p>
<% } else { %>
<table>
    <tr><th>Назва</th><th>Кількість</th><th>Ціна</th></tr>
    <%
        for (CartItem it : cart.getItems()) {
    %>
    <tr>
        <td><%= it.getComic().getTitle() %></td>
        <td><%= it.getQuantity() %></td>
        <td><%= it.getComic().getPrice() * it.getQuantity() %></td>
    </tr>
    <% } %>
    <tr><td colspan="2">Разом:</td><td><%= cart.getTotal() %></td></tr>
</table>
<p><a href="checkout.jsp">Оформити замовлення</a></p>
<% } %>
</body>
</html>
