<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, java.text.*"%>

<div class="block">
    <h2>Додати нового користувача</h2>
    <p>
        <%= request.getParameter("welcomeMessage") %>
    </p>
    <form name="f1" method="post" action="index.jsp" class="user-form">
        <label for="name">Ім'я користувача:</label> <input id="name"
                                                           name="name" type="text" maxlength="20" value="Олександр" /> <label
            for="email">Електронна пошта:</label> <input id="email" name="email"
                                                         type="email" maxlength="20" value="sunmeatrich@gmail.com" /> <input
            type="submit" name="submit" value="Додати користувача"
            class="submit-button" />
    </form>
</div>