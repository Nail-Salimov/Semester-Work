<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Store</title>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Store</title>
    <script>
        function pageList() {
            let currentPage = ${page};
            let pages = Math.ceil((${productsCount}) / 3);
            let start;
            let end;


            if (currentPage === 1) {
                start = 1;
                end = 2;

                if (pages < 2) {
                    end = 1;
                }

            } else if (currentPage === pages) {

                start = currentPage - 1;
                end = currentPage;

            } else if (currentPage > pages) {
                start = 1;
                end = 2;

                if (pages < 2) {
                    end = 1;
                }
            } else {
                start = currentPage - 1;
                end = currentPage + 1;
            }


            for (let i = start; i < end + 1; i++) {
                $("#page_bar").append(" <a href=\"/store?page=" + i + "\">" + i + "</a>");
            }
        }
    </script>
</head>
<body onload="pageList()">


<div style="background-color:lightblue">
</div>
<header>
    <p>
        <#if userData?has_content>
            <#if userData.role == "SELLER">
                |<a href="/offer_for_sale">Продажа</a>|
                <a href="/profile">Профиль</a>|
                <a href="/orders">Заказы</a>|
                <a href="/lobbies">Сообщения</a>|
            <#else>
                <a href="/profile">Профиль</a>|
                <a href="/lobbies">Сообщения</a>|
            </#if>
        <#else>
            <a href="/signIn">Авторизуйтесь</a>|
        </#if>
    </p>
</header>

<div>
    <form action="/search">
        <input type="text" name="searchText" placeholder="Поиск:"/>
    </form>
</div>


<#list listProduct as product>
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

<div id="page_bar">

</div>
</body>
</html>