<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>New Messages</title>
</head>
<body>
<#list messages as m>
    <#if m? has_content>
        <#if m?size!=0>
            <p><a href="/chat/${m[0].senderId}">Новое сообщение(${m?size})</a></p>
        </#if>
    </#if>
</#list>
</body>
</html>