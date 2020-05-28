package zzleep.communicator.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import zzleep.communicator.models.DownLinkMessage;
import zzleep.communicator.models.UpLinkMessage;

import org.springframework.stereotype.Component;
import zzleep.communicator.repository.PersistenceRepository;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Hex;

@Component
public class EmbeddedControllerImpl implements EmbeddedController, Listener {

    private PersistenceRepository repository;
    private WebSocket socket;
    private final Gson gson = new Gson();

    public EmbeddedControllerImpl(PersistenceRepository dbService) {
        this.repository = dbService;


        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        try {
            this.socket = ws.get();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Execution Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());

        reinitializeWebSocket(webSocket);
        System.out.println("onError completed");
    }

    private void reinitializeWebSocket(WebSocket webSocket) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        try {
            this.socket = ws.get();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Execution Exception");
            e.printStackTrace();
        }

        webSocket.abort();
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);

        reinitializeWebSocket(webSocket);

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
        progressThread.setDaemon(false);
        progressThread.start();

    }

    private void onProgress() {

        ArrayList<Command> commands = repository.getUpdates();

        for (Command command : commands) {
            send(command);
        }

    }

    //@Override
    public void send(Command command) {
        CharSequence charSequence = processCommand(command);
        sendText(charSequence, true);

    }

    //@Override
    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {

        socket.sendText(data, true);
        socket.request(1);

        System.out.println(data.toString());
        System.out.println("sentText completed");

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


    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String s = data.toString();
        System.out.println(s);
        UpLinkMessage message = gson.fromJson(s, UpLinkMessage.class);
        if(message.getCmd().equals("rx"))
        {
            CurrentData currentData = processData(message);
            receive(currentData);

            Command command = new Command("0004A30B002181EC", 'D', 1);
            send(command);
        }
        System.out.println("Text after the whole thing");


        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    @Override
    public void receive(CurrentData data) {

        repository.putDataInDatabase(data);

    }


    private CurrentData processData(UpLinkMessage message) {

        System.out.println(message.toString());

        //source
        CurrentData currentData = new CurrentData();
        currentData.setSource(message.getEUI());

        //timestamp
//        long timestampS = message.getTs();
//
//        LocalDateTime triggerTime =
//                LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampS), TimeZone
//                        .getDefault().toZoneId());
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formatted = formatter.format(triggerTime);


        Date date = new Date(message.getTs());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC+02:00"));
        String formatted = format.format(date);
        System.out.println("First try of converting date");
        System.out.println(formatted);


        //data
        Iterable<String> result = Splitter.fixedLength(4).split(message.getData());
        String[] parts = Iterables.toArray(result, String.class);

        String humSHex = parts[0];
        String tempSHex = parts[1];

        int temp = Integer.parseInt(humSHex, 16);
        int tempR = temp/10;
        int hum = Integer.parseInt(tempSHex,16);
        int humR = hum/10;
        int co2 = 0;
        int sound = 0;


        currentData.setTimeStamp(formatted);
        currentData.setHumidityData((double)humR);
        currentData.setTemperatureData((double)tempR);
        currentData.setCo2Data(250.0); // TODO: 5/21/2020  changes
        currentData.setSoundData(50.0);
        System.out.println(currentData.toString());


        return currentData;
    }


    private CharSequence processCommand(Command command) {

        //getCommand =  D
        //getValue = 1
        String data=""+command.getCommandID()+command.getValue();
        String hex =  Hex.encodeHexString(data.getBytes());

        DownLinkMessage message = new DownLinkMessage(command.getDestination(), true, hex);
        String json = gson.toJson(message);
        return json;

    }


}
