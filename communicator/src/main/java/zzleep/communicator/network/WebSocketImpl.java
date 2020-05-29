package zzleep.communicator.network;


import zzleep.core.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;


public class WebSocketImpl implements WebSocketHandler, WebSocket.Listener {


    private Consumer<String> listener;
    private Logger logger;
    private WebSocket socket;


    public WebSocketImpl(Consumer<String> listener, Logger logger) {
        this.listener = listener;
        this.logger = logger;
        reinitializeWebSocket(null);

    }

    @Override
    public void onOpen(WebSocket webSocket) {

        webSocket.request(1);
        logger.info("WebSocket Listener has been opened for requests.");


    }


    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        logger.error("A " + error.getCause() + " exception was thrown.");
        logger.info("Message: " + error.getLocalizedMessage());


        reinitializeWebSocket(webSocket);
        logger.info("onError completed");

    }


    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        logger.warn("WebSocket closed!");
        logger.info("Status:" + statusCode + " Reason: " + reason);


        reinitializeWebSocket(webSocket);

        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(logger::info);
    }


    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String s = data.toString();
        listener.accept(s);
        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(logger::info);
    }


    //@Override
    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {

        socket.sendText(data, true);
        socket.request(1);
        logger.info("sentText completed");

        return new CompletableFuture().newIncompleteFuture().thenAccept(System.out::println);
    }


    private void reinitializeWebSocket(WebSocket webSocket) {

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        if (webSocket != null) {
            webSocket.abort();
        }

    }


    @Override
    public void send(CharSequence data) {

        sendText(data, true);
    }

}
