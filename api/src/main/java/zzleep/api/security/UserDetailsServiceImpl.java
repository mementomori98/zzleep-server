package zzleep.api.security;

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

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // if userId exists
        //   return {userId:1234}
        // else
        //   throw UsernameNotFoundException
        UserRecord user = null;
        try {
            user = FirebaseAuth.getInstance().getUser(userId);
        } catch (FirebaseAuthException e) {
            throw new UsernameNotFoundException("User " + userId + " not found.");
        }
         return User
            .withUsername(userId)
            .password(passwordEncoder().encode("1234"))
            .roles("USER")
            .build();
        // throw new UsernameNotFoundException("User " + userId + " not found.");
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
