<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<!DOCTYPE html>
<html>
<head>
    <title>Аутентификация</title>
    <meta charset='UTF-8'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style2.css">

</head>

<body>

<header class="header">
    <a class="button7" href="registration">Зарегистрироваться</a>
    <div class="header-text">Просто сайт</div>
</header>

<form name="newUser" method="post" action="authentication">

    <input name="email"
           placeholder="Электронная почта"
           type="text"
           required
           value="${email}">
    <br>

    <input id="1"
           name="password"
           placeholder="Пароль"
           type="password"
           required
           value="${password}">
    <br>

    <div class="error">
        ${error}
    </div>

    <input type="submit" value="Войти">
</form>

</body>
</html>





