package zzleep.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zzleep.api.controllers.LoggingInterceptor;
import zzleep.core.logging.Logger;
import zzleep.core.logging.LoggerImpl;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Logger logger;

    public WebConfig(Logger logger) {
        this.logger = logger;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoggingInterceptor(logger));
//    }
}
