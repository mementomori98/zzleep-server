package zzleep.communicator.network.fakeNetwork;

import com.google.gson.Gson;

import zzleep.communicator.models.DownLinkMessage;
import zzleep.communicator.models.UpLinkMessage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class FakeDeviceSimulatorImpl implements FakeDeviceSimulator, Runnable {

    private ArrayList<String> activateDevices;
    private ArrayList<String> activateVentilation;
    private final Gson gson = new Gson();

    private Consumer<String> listener;

    public FakeDeviceSimulatorImpl(Consumer<String> listener) {
        this.listener = listener;
        this.activateDevices = new ArrayList<>();
        this.activateVentilation = new ArrayList<>();

        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    @Override
    public void simulate(DownLinkMessage message) {

        switch (message.getData()) {
            case "4431": {
                activateDevices.add(message.getEUI());
                break;
            }
            case "4430": {
                activateDevices.remove(message.getEUI());
                break;
            }
            case "5631": {
                activateVentilation.add(message.getEUI());
                break;
            }
            case "5630": {
                activateVentilation.remove(message.getEUI());
                break;
            }
            default: {
                break;
            }
        }

    }


    @Override
    public void run() {

        while (true) {

            for (String device : new ArrayList<>(activateDevices)) {

                String s = generateRandomData();

                LocalDateTime now = LocalDateTime.now();
                ZoneId zoneId = ZoneId.of("Europe/Paris");
                long epoch = now.atZone(zoneId).toEpochSecond() * 1000;


                UpLinkMessage message = new UpLinkMessage("rx", device, epoch, true, 0, 1, "", s);
                String json = gson.toJson(message);
                listener.accept(json);
            }


            try {
                Thread.sleep(10000);//10sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private String generateRandomData() {

        String r;

        int randomTemp = getRandomNumberInRange(15, 24);
        int randomCo2 = getRandomNumberInRange(200, 300);
        int randomHum = getRandomNumberInRange(38, 52);
        int randomSound = getRandomNumberInRange(30, 60);


        int tempMultiplyBy10 = randomTemp * 10;
        int humMultiplyBy10 = randomHum * 10;
        int soundMultiplyBy10 = randomSound * 10;

        String tempHex = Integer.toString(tempMultiplyBy10, 16);
        String humHex = Integer.toString(humMultiplyBy10, 16);
        String co2Hex = Integer.toString(randomCo2, 16);
        String soundHex = Integer.toString(soundMultiplyBy10, 16);

        String tempFormatted = padLeftZeros(tempHex, 4);
        String humFormatted = padLeftZeros(humHex, 4);
        String co2Formatted = padLeftZeros(co2Hex, 4);
        String soundFormatted = padLeftZeros(soundHex, 4);

        r = tempFormatted + humFormatted + co2Formatted + soundFormatted;


        return r;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
