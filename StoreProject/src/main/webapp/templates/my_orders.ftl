<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>My orders</title>


</head>
<body>

<nav>
    <a href="/store">Store</a>
</nav>

<#if orders?size == 0>
    <p>У вас нет ожидаемых товаров</p>
</#if>
<#list orders as order>

    <a href="/order/${order.id}">
        <table style="border-collapse: collapse; width: 100%;" border="1">
            <tbody>
            <tr>
                <td style="width: 50%;">
                    <a href="/product/${order.product.id}">
                        <img src="/image/${order.product.images[0].imageName}" width="200"
                             height="200"></img>
                    </a>
                </td>
                <td style="width: 50%;">
                    <p>Название: ${order.product.name}</p>
                    <p>Состояние: ${order.orderState}</p>
                    <p>Количество: ${order.count}</p>

                </td>
            </tr>
            </tbody>
        </table>
    </a>

</#list>
</body>
</html>