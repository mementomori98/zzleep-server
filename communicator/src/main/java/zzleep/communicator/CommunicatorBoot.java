package zzleep.communicator;


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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


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
        System.out.println("Hello World");

//        CurrentData currentData0 = new CurrentData(230.0, 22.0, 48.0, 30.5, "0004A30B002181EC", "2020-05-27 12:09:36");
//        embeddedController.receive(currentData0);
//        CurrentData currentData = new CurrentData(230.0, 27.5, 48.0, 30.5, "0004A30B002181EC", "2020-05-27 11:40:36");
//        embeddedController.receive(currentData);
//        CurrentData currentData1 = new CurrentData(220.0, 28.0,47.0, 45, "0004A30B002181EC", "2020-05-27 11:41:45");
//        embeddedController.receive(currentData1);



    }
}
