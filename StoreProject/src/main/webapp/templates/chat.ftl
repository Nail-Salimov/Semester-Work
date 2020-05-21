<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script
            src="https://code.jquery.com/jquery-3.4.1.min.js"
            integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
            crossorigin="anonymous"></script>
    <title>${room.id}</title>

    <script>
        function sendMessage(pageId, text) {
            let body = {
                pageId: pageId,
                text: text
            };

            $.ajax({
                url: "/api/messages/${room.id}",
                method: "POST",
                headers: {
                    Authorization: "${userData.token}"
                },
                data: JSON.stringify(body),
                contentType: "application/json",
                dataType: "json",
                complete: function () {
                   $('#message').val("");
                }
            });
        }

        // LONG POLLING
        function receiveMessage(pageId) {
            $.ajax({
                url: "/api/messages/${room.id}?pageId=" + pageId,
                method: "GET",
                dataType: "json",
                contentType: "application/json",
                headers: {
                    Authorization: "${userData.token}"
                },

                success: function (response) {

                    response.forEach(function (item, i, arr) {
                        $('#messages').first().after('<li>' + item.text + '</li>');
                    });

                    receiveMessage(pageId);
                }
            })
        }
    </script>


</head>
<body onload="receiveMessage('${userData.id}')">
<div>
    <input id="message" placeholder="Ваше сообщение">
    <button onclick="sendMessage('${userData.id}',
            $('#message').val())">Отправить</button>
</div>
<div>
    <ul id="messages">

    </ul>
</div>
</body>
</html>