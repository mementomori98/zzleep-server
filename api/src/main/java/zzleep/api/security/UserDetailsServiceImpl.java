package zzleep.api.security;

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

    // userId:1234

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // if userId exists
        //   return {userId:1234}
        // else
        //   throw UsernameNotFoundException
         return User
            .withUsername(s)
            .password(passwordEncoder().encode("1234"))
            .roles("USER")
            .build();
        // throw new UsernameNotFoundException("User " + s + " not found.");
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
