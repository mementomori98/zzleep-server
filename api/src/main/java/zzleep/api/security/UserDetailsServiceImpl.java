package zzleep.api.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static boolean firebaseInitialized = false;

    public UserDetailsServiceImpl() {
        synchronized (this) {
            if (!firebaseInitialized) {
                initializeFirebase();
                firebaseInitialized = true;
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // UserRecord user = null;
        // try {
        //     user = FirebaseAuth.getInstance().getUser(userId);
        // } catch (FirebaseAuthException e) {
        //     throw new UsernameNotFoundException("User " + userId + " not found.");
        // }
        return User
        .withUsername(userId)
        .password(passwordEncoder().encode("1234"))
        .roles("USER")
        .build();
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static void initializeFirebase() {
        InputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("api\\zzleep-firebase-key.json");
        } catch (FileNotFoundException e) {
            serviceAccount = new ByteArrayInputStream(
                System.getenv("FIREBASE_CREDENTIALS").getBytes()
            );
        }

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
