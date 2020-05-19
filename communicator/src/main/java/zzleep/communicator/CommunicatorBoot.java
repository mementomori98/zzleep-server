package zzleep.communicator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import zzleep.communicator.embedded.EmbeddedService;
import zzleep.communicator.embedded.EmbeddedServiceImpl;

@SpringBootApplication
@ComponentScan("zzleep")
public class CommunicatorBoot implements CommandLineRunner
{
    public static void main( String[] args )
    {
        SpringApplication.run(CommunicatorBoot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World");
        EmbeddedService embeddedService = new EmbeddedServiceImpl();

    }
}
