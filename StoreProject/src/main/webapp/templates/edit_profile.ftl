<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Edit Profile</title>
</head>
<body>
<nav>
    <a href="/store">Store</a>
</nav>
<p>Редактирование профиля</p>
<form action="/profile/edit" method="post">
    <p>Имя: <input name="name" type="text" value="${userData.name}"></p>
    <#if userData.address?has_content>
        <p>Адрес: <input name="address" type="text" value="${userData.address}"></p>
    <#else>
        <p>Адрес: <input name="address" type="text"></p>
    </#if>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <button type="submit">Сохранить</button>
</form>
</body>
</html>