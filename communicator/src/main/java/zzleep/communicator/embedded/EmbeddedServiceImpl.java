package zzleep.communicator.embedded;

import com.google.gson.Gson;

import models.Command;
import models.CurrentData;
import models.DownLinkMessage;
import models.UpLinkMessage;

import zzleep.communicator.databaseService.DatabaseService;
import zzleep.communicator.databaseService.DatabaseServiceImpl;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class EmbeddedServiceImpl implements EmbeddedService, Listener {

    private DatabaseService dbService;
    private WebSocket socket;
    private final Gson gson = new Gson();

    public EmbeddedServiceImpl() {

        dbService = new DatabaseServiceImpl();


        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();
    }


    public void onError​(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());

//        HttpClient client = HttpClient.newHttpClient();
//        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
//                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);
//
//        this.socket = ws.join();

        webSocket.abort();
    }

    //onClose()
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);

//        HttpClient client = HttpClient.newHttpClient();
//        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
//                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);
//
//        this.socket = ws.join();

        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }


    @Override
    public void onOpen(WebSocket webSocket) {

        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
        onStart();

    }

    private void onStart() {
        onProgress();

        Thread progressThread = new Thread(this);
        progressThread.run();

    }

    private void onProgress() {

        ArrayList<Command> commands = dbService.getUpdates();

        for (Command command : commands) {
            send(command);
        }

    }

    private void send(Command command) {
        CharSequence charSequence = processCommand(command);
        sendText(charSequence, true);

    }


    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {

        socket.sendText(data, true);
        System.out.println("sentTest completed");

        return new CompletableFuture().newIncompleteFuture().thenAccept(System.out::println);
    }


    @Override
    public void run() {

        while (true) {
            onProgress();
            try {
                Thread.sleep(300000);//5min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //onText()
    public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println(data);
        CurrentData currentData = processData(data);
        receive(currentData);
        webSocket.request(1);

        // TODO: 5/13/2020 understand CompletionStage
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    @Override
    public void receive(CurrentData data) {

        dbService.putDataInDatabase(data);

    }


    // TODO: 5/13/2020 Transform the Json telegram in CurrentData object format;
    private CurrentData processData(CharSequence data) {
        String messageString = data.toString();
        UpLinkMessage message = gson.fromJson(messageString, UpLinkMessage.class);
        System.out.println(message.toString());

        CurrentData currentData = new CurrentData();
        currentData.setSource(message.getEUI()); //source

        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(message.getTs(), 0, ZoneOffset.UTC);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = df.format(dateTime);
        currentData.setTimeStamp(timestamp);//timestamp

        // TODO: 5/13/2020 transform binary data in actual sensor data

        System.out.println(currentData.toString());


        return currentData;
    }


    // TODO: 5/13/2020 Create Json telegram

    private CharSequence processCommand(Command command) {

        StringBuffer sb = new StringBuffer();
        String hexString = Integer.toHexString(command.getCommandID());
        sb.append(hexString);
        String hexString2 = Integer.toHexString(command.getValue());
        sb.append(hexString2);

        String data = sb.toString();
        // TODO: 5/19/2020 Ask IB if we could get the EUI before the connection to store it already in DB
        DownLinkMessage message = new DownLinkMessage(command.getDestination(), true, data);
        String json = gson.toJson(message);
        return json;
        //D1, D0, V1, V0


    }


}
