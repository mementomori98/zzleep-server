package zzleep.api.controllers;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zzleep.core.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class LoggingInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger;

    public LoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.mark(0);
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        logger.info(
            String.format("%s %s", request.getMethod(), request.getRequestURI()),
            stringBuilder.toString()
        );

        // request.getInputStream().reset();

        return true;
    }
    
}
