package zzleep.communicator.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import zzleep.communicator.models.DownLinkMessage;
import zzleep.communicator.models.UpLinkMessage;

import org.springframework.stereotype.Component;
import zzleep.communicator.network.fakeNetwork.Proxy;
import zzleep.communicator.network.WebSocketHandler;
import zzleep.communicator.repository.PersistenceRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Random;
import java.util.TimeZone;


import org.apache.commons.codec.binary.Hex;
import zzleep.core.logging.Logger;

@Component
public class EmbeddedControllerImpl implements EmbeddedController{

    private PersistenceRepository repository;
    private WebSocketHandler socketHandler;
    private final Gson gson = new Gson();
    private final Logger logger;

    public EmbeddedControllerImpl(PersistenceRepository dbService, Logger logger) {
        this.repository = dbService;
        this.logger = logger;
        this.socketHandler = new Proxy(this::receiveData, logger);
        onStart();

    }


    @Override
    public void send(Command command) {
        logger.info(command.toString());
        CharSequence charSequence = processCommand(command);
        socketHandler.send(charSequence);

    }


    @Override
    public void run() {

        while (true) {
            onProgress();
            try {
                Thread.sleep(5000);//5min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void receive(CurrentData data) {

        repository.putDataInDatabase(data);

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

    private void receiveData(String s) {
        UpLinkMessage message = gson.fromJson(s, UpLinkMessage.class);
        logger.info(message.toString());

        if (message.getCmd().equals("rx")) {
            CurrentData currentData = processData(message);
            // TODO REMOVE THIS, ZOLLY ASKED FOR IT
            if (message.getEUI().equals("fake_device1")) {
               currentData.setTemperatureData(new Random().nextDouble() * 3 + 10);
            }
            logger.info(currentData.toString());
            receive(currentData);

// TODO: 5/28/2020 eliminate after figuring out the webSocket framework
            Command command = new Command("0004A30B002181EC", 'D', 1);
            send(command);
        }
    }


    private CurrentData processData(UpLinkMessage message) {

        //source
        CurrentData currentData = new CurrentData();
        currentData.setSource(message.getEUI());

        //timestamp
        long timestampS = message.getTs();
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampS), TimeZone
                        .getDefault().toZoneId());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = formatter.format(triggerTime);

        //data
        Iterable<String> result = Splitter.fixedLength(4).split(message.getData());
        String[] parts = Iterables.toArray(result, String.class);

        String tempSHex = parts[0];
        String humSHex = parts[1];
        if (parts.length==4) {
            String co2Hex = parts[2];
            String soundHex = parts[3];
        }

        int temp = Integer.parseInt(tempSHex, 16);
        int tempR = temp / 10;
        int hum = Integer.parseInt(humSHex, 16);
        int humR = hum / 10;
        int co2 = getRandomNumberInRange(400, 600);
        int sound = getRandomNumberInRange(30, 60);

        currentData.setTimeStamp(formatted);
        currentData.setHumidityData(humR);
        currentData.setTemperatureData(tempR);
        currentData.setCo2Data(co2); // TODO: 5/21/2020  changes
        currentData.setSoundData(sound);

        return currentData;
    }


    private CharSequence processCommand(Command command) {

        String data = "" + command.getCommandID() + command.getValue();
        String hex = Hex.encodeHexString(data.getBytes());

        DownLinkMessage message = new DownLinkMessage(command.getDestination(), true, hex);
        String json = gson.toJson(message);
        return json;

    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
