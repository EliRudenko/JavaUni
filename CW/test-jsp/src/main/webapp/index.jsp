<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    int defaultWidth = 180;
    int width = defaultWidth;
    int resetCount = 0;

    String action = request.getParameter("action");
    String widthParam = request.getParameter("width");
    String resetParam = request.getParameter("resetCount");

    if (widthParam != null)
    {
        try { width = Integer.parseInt(widthParam); }
        catch (NumberFormatException e) { width = defaultWidth; }
    }

    if (resetParam != null)
    {
        try { resetCount = Integer.parseInt(resetParam); }
        catch (NumberFormatException e) { resetCount = 0; }
    }

    if ("increase".equals(action)) { width += 50; }
    else if ("reset".equals(action))
    {
        width = defaultWidth;
        resetCount++;
    }
%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>JSP interactive button</title>
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
            background-color: #0d1117;
            color: #c9d1d9;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .card {
            background-color: #161b22;
            border: 1px solid #30363d;
            border-radius: 16px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.25);
            padding: 40px 60px;
            text-align: center;
            transition: all 0.3s;
        }

        h1 {
            color: #f78166;
            margin-bottom: 25px;
        }

        p {
            color: #8b949e;
            margin-bottom: 30px;
        }

        .btn {
            display: inline-block;
            color: #ffffff;
            border: none;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            font-size: 1rem;
            transition: all 0.2s ease;
            height: 50px;
        }

        .increase {
            width: <%= width %>px;
            background: #f78166;
            margin-bottom: 15px;
        }

        .reset {
            width: 180px;
            background: #8e82ff;
        }

        .btn:hover {
            filter: brightness(1.1);
        }

        .counter {
            margin-top: 15px;
            font-size: 0.9rem;
            color: #58a6ff;
        }

        footer {
            margin-top: 25px;
            font-size: 0.85rem;
            color: #8b949e;
        }
    </style>
</head>
<body>
<div class="card">
    <h1>Кнопочка</h1>
    <p>Увеличивай или сбрасывай размер</p>

    <form method="get" style="margin: 0;">
        <input type="hidden" name="width" value="<%= width %>">
        <input type="hidden" name="resetCount" value="<%= resetCount %>">

        <button class="btn increase" type="submit" name="action" value="increase">
            <%= width %> px
        </button>
        <br>
        <button class="btn reset" type="submit" name="action" value="reset">
            Сбросить
        </button>
    </form>

    <% if (resetCount > 0) { %>
    <div class="counter">Сбросов: <%= resetCount %></div>
    <% } %>

    <footer>demo</footer>
</div>
</body>
</html>
