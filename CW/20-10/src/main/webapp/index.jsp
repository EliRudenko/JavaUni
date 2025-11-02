<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Таблиця користувачів</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div class="container">

    <jsp:include page="/WEB-INF/form.jsp">
        <jsp:param name="welcomeMessage" value="Ласкаво просимо" />
    </jsp:include>

    <div class="block">
        <h2>Наявні користувачі:</h2>

        <%
            HttpSession sess = request.getSession(false);
            if (sess == null) {
                sess = request.getSession(true);
            }

            @SuppressWarnings("unchecked")
            List<String> name_array = (List<String>) sess.getAttribute("name_array");

            @SuppressWarnings("unchecked")
            List<String> email_array = (List<String>) sess.getAttribute("email_array");

            @SuppressWarnings("unchecked")
            List<Date> reg_date_array = (List<Date>) sess.getAttribute("reg_date_array");

            if (name_array == null)
            {
                name_array = new ArrayList<>();
                sess.setAttribute("name_array", name_array);
            }
            if (email_array == null)
            {
                email_array = new ArrayList<>();
                sess.setAttribute("email_array", email_array);
            }
            if (reg_date_array == null)
            {
                reg_date_array = new ArrayList<>();
                sess.setAttribute("reg_date_array", reg_date_array);
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String submit = request.getParameter("submit");

            if (submit != null && "POST".equalsIgnoreCase(request.getMethod()))
            {
                JspWriter jspOut = pageContext.getOut();

                if (name != null && !name.trim().isEmpty() && email != null && !email.trim().isEmpty())
                {
                    boolean nameExists = name_array.contains(name.trim());
                    boolean emailExists = email_array.contains(email.trim());

                    if (nameExists) { jspOut.println("<b>Користувач з таким логіном уже існує!</b><br />"); }
                    if (emailExists) { jspOut.println("<b>Користувач з такою поштою уже існує!</b><br />"); }

                    if (!nameExists && !emailExists) {
                        name_array.add(name.trim());
                        email_array.add(email.trim());
                        reg_date_array.add(new Date());

                        response.sendRedirect(request.getRequestURI());
                        return;
                    }

                } else {
                    if (name == null || name.trim().isEmpty()) { jspOut.println("<b>Не вказано логін!</b><br />"); }
                    if (email == null || email.trim().isEmpty()) { jspOut.println("<b>Не вказано пошту!</b><br />"); }
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("uk", "UA"));
        %>

        <table class="user-table">
            <tr>
                <th>Ім'я</th>
                <th>Пошта</th>
                <th>Час реєстрації</th>
            </tr>

            <%
                for (int i = 0; i < name_array.size(); i++) {
            %>
            <tr>
                <td><%= name_array.get(i) %></td>
                <td><%= email_array.get(i) %></td>
                <td><%= sdf.format(reg_date_array.get(i)) %></td>
            </tr>
            <%
                }
            %>
        </table>
    </div>
</div>
</body>
</html>
