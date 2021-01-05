<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${search_word}</title>
</head>
<body>
<nav>
    <a href="/store">Store</a>
</nav>
<div style="background-color:lightblue">
</div>

<div>
    <form action="/search">
        <input type="text" name="searchText"/>
    </form>
</div>

<div>
    <#if userData?has_content>
        <#if userData.role == "SELLER">
            <a href="/offer_for_sale">Продажа</a>
            <p><a href="/profile">Профиль</a></p>
        <#else>
            <p>Приятных покупок</p>
            <p><a href="/profile">Профиль</a></p>
        </#if>
    <#else>
        <a href="/signIn">Авторизуйтесь</a>
    </#if>
</div>

<#list products as product>
    <a href="/product/${product.id}">
        <table style="border-collapse: collapse; width: 100%;" border="1">
            <tbody>
            <tr>
                <td style="width: 50%;"><img src="/image/${product.images[0].imageName}" width="200" height="200"></img>
                </td>
                <td style="width: 50%;">
                    <table style="border-collapse: collapse; width: 100%;" border="1">
                        <tbody>
                        <tr>
                            <td style="width: 100%;"><p>${product.name}</p></td>
                        </tr>
                        <tr>
                            <td style="width: 100%;"><p>${product.description}</p></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </a>
</#list>
</body>
</html>