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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

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



    }
}
