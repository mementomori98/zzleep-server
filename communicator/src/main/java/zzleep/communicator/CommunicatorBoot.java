package zzleep.communicator;


import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import zzleep.communicator.controller.EmbeddedController;
import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import zzleep.communicator.models.UpLinkMessage;
import zzleep.core.etl.EtlProcess;
@SpringBootApplication
@ComponentScan("zzleep")
public class CommunicatorBoot implements CommandLineRunner
{
    public CommunicatorBoot(EmbeddedController embeddedController) {
        this.embeddedController = embeddedController;
    }

    public static void main(String[] args )
    {
        SpringApplication.run(CommunicatorBoot.class, args);
    }

    private EmbeddedController embeddedController;

    @Override
    public void run(String... args) throws Exception {
        new Thread(new EtlProcess()).start();


        String source = "0004A30B002181EC";
        String data4Length = "00dc04b000000dac";
        String data2Length = "00dc01c2";

        ZoneId zoneId = ZoneId.of("Europe/Paris");
        LocalDateTime now = LocalDateTime.now();
        long epoch = now.atZone(zoneId).toEpochSecond() * 1000;
        LocalDateTime now2 = LocalDateTime.now();
        long epoch2 = now2.atZone(zoneId).toEpochSecond() * 1000;


        UpLinkMessage message1 = new UpLinkMessage("rx", source, epoch,true,0,1,"",data4Length) ;
        Gson gson = new Gson();
        String json1 = gson.toJson(message1);
        UpLinkMessage message2 = new UpLinkMessage("rx", source, epoch2,true,0,1,"",data2Length) ;
        String json2 = gson.toJson(message2);

        embeddedController.receiveData(json1);
        embeddedController.receiveData(json2);

    }
}
