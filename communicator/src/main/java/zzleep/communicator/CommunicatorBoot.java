package zzleep.communicator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import zzleep.communicator.embedded.EmbeddedService;
import zzleep.core.etl.EtlProcess;

@SpringBootApplication
@ComponentScan("zzleep")
public class CommunicatorBoot implements CommandLineRunner
{
    public CommunicatorBoot(EmbeddedService embeddedService) {
        this.embeddedService = embeddedService;
    }

    public static void main(String[] args )
    {
        SpringApplication.run(CommunicatorBoot.class, args);
    }

    private EmbeddedService embeddedService;
    @Override
    public void run(String... args) throws Exception {
        new Thread(new EtlProcess()).start();
        System.out.println("Hello World");
    }
}
