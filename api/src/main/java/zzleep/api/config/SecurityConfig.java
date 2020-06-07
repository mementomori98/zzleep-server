package zzleep.api.config;

// import mox.bigtalk.security.BasicAuthenticationEndpointImpl;
// import mox.bigtalk.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import zzleep.api.security.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final UserDetailsServiceImpl userDetailsService;

        public ApiWebSecurityConfigurationAdapter(UserDetailsServiceImpl userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests(a -> a.anyRequest().authenticated())
                .httpBasic(withDefaults());
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {

            auth.userDetailsService(userDetailsService);
        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .antMatcher("/swagger-ui.html")
                .authorizeRequests(a -> a.anyRequest().permitAll());
        }
    }
}
