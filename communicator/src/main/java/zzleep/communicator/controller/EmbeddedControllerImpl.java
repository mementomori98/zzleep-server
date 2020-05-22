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
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        webSocket.abort();
        System.out.println("onError completed");
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSwQAAABFpb3RuZXQudGVyYWNvbS5ka5CGv5WoQH5B19isf4NMr3s="), this);

        this.socket = ws.join();

        webSocket.abort();

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


    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println(data);
        String s = data.toString();
        UpLinkMessage message = gson.fromJson(s, UpLinkMessage.class);
        if(message.getCmd().equals("rx"))
        {
            CurrentData currentData = processData(data);
            receive(currentData);
        }

        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    @Override
    public void receive(CurrentData data) {

        repository.putDataInDatabase(data);

    }


    private CurrentData processData(CharSequence data) {
        String messageString = data.toString();
        UpLinkMessage message = gson.fromJson(messageString, UpLinkMessage.class);
        System.out.println(message.toString());

        //source
        CurrentData currentData = new CurrentData();
        currentData.setSource(message.getEUI());

        //timestamp
        long timestampS = message.getTs();
        Date date = new Date(timestampS);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC")); // TODO: 5/21/2020 2020-05-21 09:03:22 instead of 11:03:22 check with UTC+02:00
        String formatted = format.format(date);
        System.out.println("First try of converting date");
        System.out.println(formatted);

//        ZonedDateTime dateTime = Instant.ofEpochSecond(timestampS).atZone(ZoneId.of("+01:00"));
//        String formatted2 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        System.out.println("Second try of converting date");
//        System.out.println(formatted2);

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


//        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(message.getTs(), 0, ZoneOffset.UTC);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String timestamp = df.format(dateTime);
//        currentData.setTimeStamp(timestamp);//timestamp
        currentData.setTimeStamp(formatted);
        currentData.setHumidityData((double)humR);
        currentData.setTemperatureData((double)tempR);
        currentData.setCo2Data(250.0); // TODO: 5/21/2020  changes
        currentData.setSoundData(50.0);
        System.out.println(currentData.toString());


        return currentData;
    }


    private CharSequence processCommand(Command command) {

        String data=""+command.getCommandID()+command.getValue();
        String hex =  Hex.encodeHexString(data.getBytes());

        DownLinkMessage message = new DownLinkMessage(command.getDestination(), true, data);
        String json = gson.toJson(message);
        return json;

    }


}
