<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Регистрация</title>
        <meta charset='UTF-8'>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style2.css">
    </head>

    <body>

        <header class="header">
            <a class="button7" href="authentication">Войти</a>
            <div class="header-text">Просто сайт</div>
        </header>

        <form name="addUser" method="post" action="registration">

            <input name="email"
                   placeholder="Электронная почта"
                   type="email"
                   required
                   value="${email}">
            <br>

            <input name="password"
                   placeholder="Пароль"
                   type="password"
                   required
                   value="${password}">
            <br>

            <input name="password2"
                   placeholder="Повторите пароль"
                   type="password"
                   required
                   value="${password2}">
            <br>

            <div class="error">
                ${error}
            </div>

            <input type="submit" value="Зарегистрироваться">
        </form>

    </body>
</html>





