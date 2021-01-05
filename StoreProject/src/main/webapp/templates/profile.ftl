<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Profile</title>
</head>
<body>
<p><a href="/store">Store</a></p>
<p><a href="/my_orders">Мои заказы</a></p>
<p>Имя: ${userData.name}</p>
<p>Почта: ${userData.mail}</p>
<p>Роль: ${userData.role}</p>
<p>Адрес:
    <#if userData.address?has_content>
        ${userData.address}
        <#else>
        не задан
    </#if>
</p>


<a href="/profile/edit">Редактировать</a>
</body>
</html>