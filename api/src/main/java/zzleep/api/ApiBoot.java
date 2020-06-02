package zzleep.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;

@SpringBootApplication
@ComponentScan("zzleep")
public class ApiBoot
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApiBoot.class, args);
    }
}
