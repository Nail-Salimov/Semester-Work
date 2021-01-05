<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="/webjars/jquery/3.1.0/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/1.0.2/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3/stomp.min.js"></script>

    <style>
        * {
            box-sizing: border-box;
        }

        header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            background-color: #eef;
            padding: 1rem;
            height: 5rem;
        }

        aside {
            position: fixed;
            top: 5rem;
            bottom: 0;
            left: 0;
            background-color: #efe;
            padding: 1rem;
            width: 6rem;
        }

        footer {
            position: fixed;
            bottom: 0;
            left: 6rem;
            right: 0;
            background-color: #fee;
            padding: 1rem;
            height: 10rem;
        }

        .content {
            margin-top: 5rem;
            margin-left: 6rem;
            margin-bottom: 11rem;
        }
    </style>

    <script>
        function connect() {
            let headerName = "${_csrf.headerName}";
            let token = "${_csrf.token}";
            var headers = {};
            headers [headerName] = token;

            var socket = new SockJS('/ws/chat-messaging');
            stompClient = Stomp.over(socket);
            stompClient.connect(headers, function (frame) {
                console.log("connected: " + frame);
                stompClient.subscribe('/ws/chat/messages/${room.id}', function (response) {
                    var data = JSON.parse(response.body);
                    draw("left", data.user, data.message);
                    readMessage(data.messageId);
                });
            });
        }

        function draw(side, user, text) {


            console.log("drawing...");
            var $message;
            $message = $($('.message_template').clone().html());
            $message.addClass(side).find('.text').html(user.name + " " + text);
            $('.messages').append($message);
            return setTimeout(function () {
                return $message.addClass('appeared');
            }, 0);

        }

        function disconnect() {
            stompClient.disconnect();
        }

        function sendMessage() {
            if ($("#message_input_value").val() != "") {
                stompClient.send("/ws/app/message", {}, JSON.stringify({
                    'message': $("#message_input_value").val(),
                    "from": "${userData.id}",
                    "roomId": ${room.id}
                }));
                $("#message_input_value").val("");
            }
        }

        function readMessage(messageId) {
            stompClient.send("/ws/app/read_message", {}, JSON.stringify({
                "messageId": messageId,
                "from": "${userData.id}",
                "roomId": ${room.id}
            }));
        }
    </script>

</head>
<body onload="connect()">
<header>
    <h1>Чат с ${otherUser.getName()}</h1>
</header>
<aside>
    Меню
</aside>

<div class="chat_window content">
    <ul class="messages">
        <#if messages? has_content>
            <#list messages as message>
                <li class="message">
                    <div class="avatar"></div>
                    <div class="text_wrapper">
                        <div class="text">${message.userDataDto.name} ${message.text}</div>
                    </div>
                </li>
            </#list>
        </#if>
    </ul>
</div>
<div id="message_template" class="message_template">

    <li class="message">
        <div class="avatar"></div>
        <div class="text_wrapper">
            <div class="text"></div>
        </div>
    </li>
</div>
<footer>
    <div class="bottom_wrapper clearfix">
        <div class="message_input_wrapper">
            <input id="message_input_value" class="message_input" placeholder="Type your message here..."/>
        </div>
        <div class="send_message">
            <div class="icon"></div>

        </div>

        <button onclick="sendMessage()" class="text">Send</button>

    </div>
</footer>

</body>
</html>