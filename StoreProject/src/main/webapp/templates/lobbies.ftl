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

                stompClient.subscribe('/ws/chat/lobby/${userData.id}', function (response) {
                    var data = JSON.parse(response.body);
                    dispatcher(data);
                });
                send();
            });
        }

        function dispatcher(data) {
            let type = data.type;
            console.log("Type: " + type);
            if (type == "show") {
                drawList(data.lobbies);
            } else if (type == "new") {
                update(data.lobbies);
            } else if(type == "read"){
                read(data.lobbies);
            }
        }

        function send() {
            //stompClient.send("/ws/app/im", {}, "");
        }

        function read(data) {
            data.forEach(function (item, i, arr) {
                let lobby = $("#room_" + item.roomId);

                if (lobby.length > 0) {
                    let link = $("#link_room_" + item.roomId);
                    let text = link.text();
                    let index1 = text.lastIndexOf('(');
                    text = text.substr(0, index1);
                    link.text(text + "(0)");
                }
            })
        }

        function update(data) {
            data.forEach(function (item, i, arr) {
                let lobby = $("#room_" + item.roomId);

                if (lobby.length > 0) {
                    let link = $("#link_room_" + item.roomId);
                    let text = link.text();
                    let index1 = text.lastIndexOf('(');
                    let index2 = text.lastIndexOf(')');
                    let newCount = item.count + Number(text.substr(index1 + 1, index2 - index1 - 1));
                    link.text(item.user.name + ": " + item.lastText + " (" + newCount + ")");

                } else {
                    let lobby = $("#lobby_example").clone();
                    let pattern = "room_" + item.roomId;
                    lobby.attr("id", pattern);
                    let link = $("#link_example").clone();
                    link.attr("id", "link_room_" + item.roomId);
                    link.attr("href", "/chat/" + item.roomId);
                    link.text(item.user.name + ": " + item.lastText + " (" + item.count + ")");
                    lobby.appendTo("#lobby");
                    link.appendTo("#" + pattern);
                }
            })
        }

        function drawList(data) {
            data.forEach(function (item, i, arr) {
                let lobby = $("#lobby_example").clone();
                let pattern = "room_" + item.roomId;
                lobby.attr("id", pattern);
                let link = $("#link_example").clone();
                link.attr("id", "link_room_" + item.roomId);
                link.attr("href", "/chat/" + item.roomId);
                link.text(item.user.name + ": " + item.lastText + " (" + item.count + ")");
                lobby.appendTo("#lobby");
                link.appendTo("#" + pattern);
            })
        }

    </script>

</head>
<body onload="connect()">
<header>
    <a href="/store">Store</a>|
    <a href="/profile">Профиль</a>|
</header>
<div id="lobby">
    <#if lobbies?has_content>
        <#list lobbies as lobby>
            <div id="room_${lobby.roomId}">
                <a id="link_room_${lobby.roomId}" href="/chat/${lobby.roomId}">${lobby.user.name}: ${lobby.lastText} (${lobby.count})</a>
            </div>
        </#list>
    </#if>
</div>

<div id="lobby_example">

</div>
<a id="link_example" href="/chat/"></a>
</body>
</html>