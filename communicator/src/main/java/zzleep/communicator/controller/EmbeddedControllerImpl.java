package zzleep.communicator.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;


import zzleep.communicator.controller.commandProtocolHandler.SendCommandProtocolHandler;
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

import java.util.TimeZone;


import org.apache.commons.codec.binary.Hex;
import zzleep.core.logging.Logger;

@Component
public class EmbeddedControllerImpl implements EmbeddedController {

    private PersistenceRepository repository;
    private WebSocketHandler socketHandler;
    private CommandsService commandsService;
    private SendCommandProtocolHandler commandProtocol;
    private final Gson gson = new Gson();
    private final Logger logger;

    public EmbeddedControllerImpl(PersistenceRepository dbService, Logger logger, CommandsService commandsService, SendCommandProtocolHandler commandProtocol) {
        this.repository = dbService;
        this.logger = logger;
        this.commandsService = commandsService;
        this.socketHandler = new Proxy(this::receiveData, logger);
        this.commandProtocol = commandProtocol;
        onStart();

    }



    private void send(Command command) {
        logger.info("Sending command... " + command.toString());
        CharSequence charSequence = processCommand(command);
        socketHandler.send(charSequence);
    }


    @Override
    public void run() {

        while (true) {
            onProgress();
            try {
                Thread.sleep(5000);//5sec
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
        Thread progressThread = new Thread(this);
        progressThread.setDaemon(false);
        progressThread.start();

    }

    private void onProgress() {

        ArrayList<Command> commands = commandsService.getUpdates();

        for (Command command : commands) {
            if (commandProtocol.shouldSend(command))
                send(command);
        }

    }

    public void receiveData(String s) {
        UpLinkMessage message = gson.fromJson(s, UpLinkMessage.class);
        if (message.getCmd().equals("rx")) {
            if (shouldReceive(message))
                receiveMessage(message);
            if (commandProtocol.hasPendingCommand()) {
                send(commandProtocol.getPendingCommand());
            }
        }
    }

    private void receiveMessage(UpLinkMessage message) {
        CurrentData currentData = processData(message);
        logger.info("Received message: " + currentData.toString());
        receive(currentData);
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
        String co2Hex = parts[2];
        String soundHex = parts[3];


        int temp = Integer.parseInt(tempSHex, 16);
        int tempR = temp / 10;
        int hum = Integer.parseInt(humSHex, 16);
        double humR = (double) hum / 10;
        int co2 = Integer.parseInt(co2Hex, 16);
        int soundR = Integer.parseInt(soundHex, 16);
        double sound = (double) soundR / 10;

        currentData.setTimeStamp(formatted);
        currentData.setHumidityData(humR);
        currentData.setTemperatureData(tempR);
        currentData.setCo2Data(co2);
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


    private boolean shouldReceive(UpLinkMessage data) {
        Iterable<String> result = Splitter.fixedLength(4).split(data.getData());
        String[] parts = Iterables.toArray(result, String.class);

        if (parts.length == 4)
            return true;
        logger.warn("Message should not be received");
        return false;
    }

}
