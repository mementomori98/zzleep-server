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



        System.out.println("Date test:");
        long seconds = 1590501769;




//        //format.setTimeZone(TimeZone.getTimeZone("Etc/UTC")); // TODO: 5/21/2020 2020-05-21 09:03:22 instead of 11:03:22 check with UTC+02:00
//        format.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
//        String display0 = TimeZone.getTimeZone("Etc/UTC").getDisplayName();
//        System.out.println("Display0: "+display0);
//        String display = TimeZone.getTimeZone("Denmark/Copenhagen").getDisplayName();
//        System.out.println("Display: " + display);
//        String display2 = TimeZone.getTimeZone("Etc/UTC+02:00").getDisplayName();
//        System.out.println("Display2: "+display2);
//        //String formatted = format.format(date);
//        System.out.println("Data conversion test:");
//       // System.out.println(formatted);

//        Command command = new Command("0004A30B002181EC", 'D', 1);
//        embeddedController.send(command);

    }
}
