package zzleep.communicator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import zzleep.communicator.controller.EmbeddedController;

import zzleep.core.etl.EtlProcess;

@SpringBootApplication
@ComponentScan("zzleep")
public class CommunicatorBoot implements CommandLineRunner {
    public CommunicatorBoot(EmbeddedController embeddedController) {
        this.embeddedController = embeddedController;
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunicatorBoot.class, args);
    }

    private EmbeddedController embeddedController;

    @Override
    public void run(String... args) throws Exception {
        new Thread(new EtlProcess()).start();


    }
}
