<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${productData.name}</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"
          integrity="sha256-3dkvEK0WLHRJ7/Csr0BZjAWxERc5WH7bdeUya2aXxdU= sha512-+L4yy6FRcDGbXJ9mPG8MT/3UCDzwR9gPeyFNMCtInsol++5m3bk2bXWKdZjvybmohrAsn3Ua5x8gfLnbE1YkOg=="
          crossorigin="anonymous">
    <script src="/static/rater.js" type="application/javascript"></script>


    <script>
        function buy() {
            let count = $("#count").val();
            let data = {"id": ${productData.id}, "count": count};

            $.ajax({

                type: 'POST',
                url: 'http://localhost:8080/api/buy',
                contentType: "application/json",

                headers: {
                    <#if userData?has_content>
                    Authorization: "${userData.token}"
                    </#if>
                },

                data: JSON.stringify(data),
                dataType: 'json',

            })
                .done(function (response) {
                    $(".form").remove();
                    $("#result").empty().text(response.description);
                    let currentCount = ${productData.count} -count;
                    $("#currentCount").empty().text("Количество: " + currentCount);
                })
                .fail(function () {
                    alert('Error')
                });
        }

        function writeReview(myStars, text) {
            let data = {'text': text, 'stars': myStars, 'productId': ${productData.id}}
            var newSize = 0;

            $.ajax({

                type: 'POST',
                url: 'http://localhost:8080/api/review',
                contentType: "application/json",

                headers: {
                    <#if userData?has_content>
                    Authorization: "${userData.token}"
                    </#if>
                },

                data: JSON.stringify(data),
                dataType: 'json',

            })
                .done(function (response) {

                    starsCount++;
                    stars += myStars;

                    var str = parseFloat((stars / starsCount).toFixed(1));
                    var optionsRates = {
                        max_value: 5,
                        step_size: 0.5,
                        initial_value: str,
                        selected_symbol_type: 'utf8_star', // Must be a key from symbols
                        cursor: 'default',
                        readonly: true,
                    }

                    let newOptions = {
                        max_value: 5,
                        step_size: 0.5,
                        initial_value: myStars,
                        selected_symbol_type: 'utf8_star', // Must be a key from symbols
                        cursor: 'default',
                        readonly: true,
                    }

                    let newP = $("<p/>");
                    <#if userData?has_content>
                    newP.text('${userData.name}' + ": " + text);
                    </#if>
                    $("#rate_list").prepend(newP);

                    let newDiv = $("<div/>")
                    newDiv.addClass("rate_new_" + newSize);
                    $("#rate_list").prepend(newDiv);
                    $(".rate_new_" + newSize).rate(newOptions);
                    newSize++;

                    $(".rate_product").remove();
                    let div = $("<div/>")
                    div.addClass("rate_product");
                    $(".product_data").prepend(div);
                    $(".rate_product").rate(optionsRates);

                })
                .fail(function () {
                    alert('Error')
                });
        }

    </script>

    <script>
        function f() {

            let maxCost = #{productData.maxCost};
            let minCost =  #{productData.minCost};
            let decrease =  #{productData.decrease};


            var ms = new Date();
            const currentTime = Math.ceil(ms.getTime() / 1000);
            let productTime = #{productData.time};

            let timer = currentTime - productTime;
            let hour = Math.ceil(timer / (60 * 60)) - 1;

            if (maxCost - (hour * decrease) >= minCost) {
                $("#cost").text(maxCost - (hour * decrease));

                t();
            } else {

                $("#cost").empty().text(minCost);
                $("#timer").empty().text("Товар достиг наименьшего значения");

            }

            function t() {
                timer = timer % (60 * 60);

                let minute = Math.ceil(timer / 60);
                minute = 60 - minute;
                let sec = timer % 60;
                sec = 60 - sec;

                let time = $('.seconds');
                time.text(sec)
                let min = $('.minute');
                min.text(minute);

                intervalId = setInterval(timerDecrement, 1000);


                function timerDecrement() {
                    let newTime = time.text() - 1;
                    let newMin = min.text();
                    time.text(newTime);

                    if (newTime === 0) {
                        time.text(59);


                        if (newMin == 0) {
                            hour++;
                            if (checkCost()) {
                                min.text(59);
                            } else {
                                clearInterval(intervalId);
                            }
                        } else {
                            min.text(min.text() - 1);
                        }
                    }
                }

                function checkCost() {
                    if (maxCost - (hour * decrease) >= minCost) {
                        $("#cost").text(maxCost - (hour * decrease));
                        return true;
                    } else {
                        $("#timer").empty().text("Товар достиг наименьшего значения");
                        $("#cost").text(minCost);
                        return false;
                    }
                }
            }
        }

    </script>


</head>
<body onload="f()">
<nav>
    <a href="/store">Store</a>
    <#if userData?has_content>
        <a href="/profile">Профиль</a>|
        <a href="/lobbies">Сообщения</a>|
    <#else>
        <a href="/signIn">Авторизуйтесь</a>|
        <a href="/lobbies">Сообщения</a>|
    </#if>
</nav>

<div>
    <#list productData.images as image>
        <img src="/image/${image.imageName}" width="200" height="200"/>
    </#list>
</div>
<div class="product_data">
    <div class="rate_product"></div>
    <p>Название: ${productData.name}</p>
    <p>Описание: ${productData.description}</p>
    <p><span>Цена:</span><span id="cost"> ${productData.maxCost}</span></p>
    <p>Наименьшая цена: ${productData.minCost}</p>
    <p id="currentCount">Количество: ${productData.count}</p>
    <div id="timer">
        <span>Цена уменьшится на ${productData.getDecrease()} через: </span>
        <span class="minute">10</span><span>:</span><span class="seconds">20</span>
    </div>
</div>

<div>
    <#if userData?has_content>
        <p><a href="/writeTo/${productData.sellerId}">Напишите продавцу</a></p>
    </#if>
</div>

<div><p id="result"></p></div>
<div class="form">
    <#if userData?has_content>
        <#if userData.address?has_content>

            <input id="count" name="count" type="number" placeholder="количество:"/>
            <button onclick="buy()">Купить</button>

        <#else>
            <p>Чтобы приобрести товар, введите адрес доставки в <a href="/profile">профиле</a></p>
        </#if>

    <#else >
        <p>Чтобы приобрести товар, <a href="/signIn">авторизуйтесь</a></p>
    </#if>
</div>

<script>
    var myRate;

    $(document).ready(function () {
        var options = {
            max_value: 5,
            step_size: 0.5,
            initial_value: 0,
            selected_symbol_type: 'utf8_star', // Must be a key from symbols
            cursor: 'default',
            readonly: false,

        }

        $(".rate").rate(options);

        $(".rate").on("change", function (ev, data) {
            myRate = data.to;
        })
    });

    var starsCount;
    var stars;
    $(document).ready(function () {
        starsCount = ${productData.starsCount};
        stars = ${productData.stars};
        let str;
        if (starsCount > 0) {
            str = parseFloat((stars / starsCount).toFixed(1));
        } else {
            str = 0;
        }


        var productOptions = {
            max_value: 5,
            step_size: 0.5,
            initial_value: str,
            selected_symbol_type: 'utf8_star', // Must be a key from symbols
            cursor: 'default',
            readonly: true,
        }

        $(".rate_product").rate(productOptions);
    });

    function setRate(rateId, rate) {
        var optionsRate = {
            max_value: 5,
            step_size: 0.5,
            initial_value: rate,
            selected_symbol_type: 'utf8_star', // Must be a key from symbols
            cursor: 'default',
            readonly: true,
        }
        $(".rate_" + rateId).rate(optionsRate);

    }

    function sendReview(text) {
        if (myRate == 0.0) {
            alert("укажите свой рейтинг товару")
        } else {
            if (text == '') {
                alert("напишите отзыв")
            } else {
                writeReview(myRate, text);
            }
        }
    }
</script>
<style>

    .rate-base-layer {
        color: #aaa;
    }

    .rate-hover-layer {
        color: orange;
    }

    .rate2 {
        font-size: 35px;
    }

    .rate2 .rate-hover-layer {
        color: pink;
    }

    .rate2 .rate-select-layer {
        color: red;
    }


</style>

<#if userData?has_content>
    <div class="rate"></div>
    <input id="review_text" name="review_text"/>
    <button onclick=sendReview($("#review_text").val())></button>
</#if>
<div id="rate_list">
    <#if productData.reviews?has_content>
        <#list productData.reviews as review>
            <div id="review_${review.getId()}">
                <div class="rate_${review.getId()}"></div>
                <p>${review.userDataDto.name}: ${review.text}</p>
                <script>
                    $(document).ready(function () {
                        setRate(${review.getId()}, parseFloat('${review.stars}'.replace(',', '.').replace(' ', '')));
                    });
                </script>
            </div>
        </#list>
    </#if>
</div>
</body>
</html>