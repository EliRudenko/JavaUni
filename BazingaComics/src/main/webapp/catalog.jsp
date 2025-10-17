<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.bazinga.model.Comic" %>
<html>
<head>
    <title>Каталог коміксів</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<h2>Каталог коміксів</h2>

<%
    List<Comic> comics = (List<Comic>) request.getAttribute("comics");
    if (comics == null) { comics = new ArrayList<>(); }
    for (Comic comic : comics) {
%>
<div class="comic-card">
    <h3><%= comic.getTitle() %></h3>
    <p>Автор: <%= comic.getAuthor() %></p>
    <p>Ціна: <%= comic.getPrice() %> ₴</p>
    <form action="cart" method="post">
        <input type="hidden" name="comicId" value="<%= comic.getId() %>"/>
        Кількість: <input type="number" name="quantity" value="1" min="1" style="width:50px;"/>
        <button type="submit">Додати в кошик</button>
    </form>
</div>
<hr/>
<% } %>
</body>
</html>
