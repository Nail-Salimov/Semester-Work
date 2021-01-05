<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Orders</title>

</head>
<body>
<nav>
    <a href="/store">Store</a>
</nav>

<#if orders?size == 0>
    <p>На данный момент нет заказов</p>
</#if>
<#list orders as order>
    <table>
        <tbody>
        <tr>
            <td style="width: 50%;">
                <div>
                    <p>${order.product.name}</p>
                    <p>${order.product.count}</p>
                </div>
            </td>
            <td style="width: 50%;">
                <div>
                    <p>${order.buyer.name}</p>
                    <p>${order.buyer.address}</p>
                    <p>${order.orderState}</p>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</#list>
</body>
</html>