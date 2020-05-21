<!doctype html>

<html lang="RU">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Order</title>

    <script>
        function confirm() {
            let id = {"id": ${order.id}};
            $.ajax({

                type: 'POST',
                url: 'http://localhost:8080/api/order',
                contentType: "application/json",

                headers: {
                    Authorization: "${userData.token}"
                },

                data: JSON.stringify(id),
                dataType: 'json',

            })
                .done(function (response) {
                    $(".form").remove();
                    $("#orderState").empty().text(response.description);
                })
                .fail(function () {
                    alert('Error')
                });
        }
    </script>

</head>
<body>
<nav>
    <a href="/store">Store</a>
</nav>

<p>${order.product.name}</p>
<p>${order.count}</p>
<p id="orderState">${order.orderState}</p>
<#if order.orderState != "ACCEPTED">
<div class="form">
    <button onclick="confirm()">Пришло</button>
</div>
</#if>

</body>
</html>