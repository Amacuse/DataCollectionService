// Setup the websocket connection and event
$(function () {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var dateSocket = new WS('@routes.WebSocketController.eventWs().webSocketURL(request)');

    var receiveEvent = function (event) {

        $("#count").html(parseInt($("#count").html(), 10) + 1);
        $("#row-data").append('<tr>' + event.data + '</tr>');
    };

    dateSocket.onmessage = receiveEvent;
});
