package zzleep.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@ComponentScan("zzleep")
public class ApiBoot
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApiBoot.class, args);
        //
        // FileInputStream serviceAccount =
        //         null;
        // try {
        //     serviceAccount = new FileInputStream("api/zzleep-firebase-key.json");
        // } catch (FileNotFoundException e) {
        //     e.printStackTrace();
        // }
        //
        // FirebaseOptions options = null;
        // try {
        //     options = new FirebaseOptions.Builder()
        //             .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        //             .build();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        //
        // FirebaseApp.initializeApp(options);

    }
}
