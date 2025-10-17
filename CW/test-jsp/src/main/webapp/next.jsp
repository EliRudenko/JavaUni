<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вторая страница</title>
    <style>
        body {
            margin: 0;
            background-color: #0d1117;
            color: #c9d1d9;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .box {
            background-color: #161b22;
            border: 1px solid #30363d;
            border-radius: 16px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.2);
            padding: 40px 60px;
            text-align: center;
            width: 380px;
        }

        h2 {
            color: #f78166;
            margin-bottom: 10px;
        }

        p {
            color: #8b949e;
        }

        a {
            display: inline-block;
            margin-top: 25px;
            padding: 12px 28px;
            background-color: #2f81f7;
            color: #fff;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s, transform 0.2s;
        }

        a:hover {
            background-color: #1f6feb;
            transform: translateY(-2px);
        }

        footer {
            margin-top: 20px;
            font-size: 0.85rem;
            color: #8b949e;
        }
    </style>
</head>
<body>
<div class="box">
    <h2>Тёмная сторона</h2>
    <p>Добро пожаловать во вторую страницу</p>
    <a href="index.jsp">Назад</a>
</div>
</body>
</html>
