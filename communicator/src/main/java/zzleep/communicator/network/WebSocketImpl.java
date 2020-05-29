package zzleep.communicator.network;


import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;


public class WebSocketImpl implements WebSocketHandler, WebSocket.Listener {


    private Consumer<String> listener;
    private WebSocket socket;

    public WebSocketImpl(Consumer<String> listener) {
        this.listener = listener;


        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

    }

    @Override
    public void onOpen(WebSocket webSocket) {

        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");


    }


    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());

        reinitializeWebSocket(webSocket);
        System.out.println("onError completed");
    }



    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);

        reinitializeWebSocket(webSocket);

        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }


    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String s = data.toString();
        listener.accept(s);
        System.out.println("Text after the whole thing");


        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }


    //@Override
    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {

        socket.sendText(data, true);
        socket.request(1);
        System.out.println("sentText completed");

        return new CompletableFuture().newIncompleteFuture().thenAccept(System.out::println);
    }


    private void reinitializeWebSocket(WebSocket webSocket) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        webSocket.abort();
    }


    @Override
    public void send(CharSequence data) {

        sendText(data, true);
    }

}
