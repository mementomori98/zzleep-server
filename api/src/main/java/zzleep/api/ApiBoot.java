package zzleep.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("zzleep.core")
public class ApiBoot
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApiBoot.class, args);
    }
}
