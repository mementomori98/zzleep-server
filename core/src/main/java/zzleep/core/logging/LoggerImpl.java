package zzleep.core.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import zzleep.core.settings.DataSettings;
import zzleep.core.settings.EnvironmentSettings;

import java.util.Arrays;

@Component
public class LoggerImpl implements Logger {

    private RestTemplate restTemplate;

    public LoggerImpl(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Override
    public void log(String message, LogType type) {
        log(message, type, null);
    }

    @Override
    public void log(String message, LogType type, Object body) {
        System.out.println(makeLogMessage(message, type, body));
    }

    @Override
    public void info(String message) {
        log(message, LogType.INFO);
    }

    @Override
    public void info(String message, Object body) {
        log(message, LogType.INFO, body);
    }

    @Override
    public void warn(String message) {
        log(message, LogType.WARNING);
    }

    @Override
    public void warn(String message, Object body) {
        log(message, LogType.WARNING, body);
    }

    @Override
    @Async
    public void error(String message) {
        log(message, LogType.ERROR);
        logToSlack(message, LogType.ERROR);
    }

    @Override
    @Async
    public void error(String message, Object body) {
        log(message, LogType.ERROR, body);
        logToSlack(message, LogType.ERROR, body);
    }

    private String makeLogTypeString(LogType type) {
        if (type == null) return "";
        return "[" + type.toString() + "]";
    }

    private String makeLogMessage(String message, LogType type, Object body) {
        String s = makeLogTypeString(type) + " " + message;
        if (body != null)
            s += "\n - body: " + toJson(body);
        return s;
    }

    private String makeLogMessage(String message, LogType type) {
        return makeLogMessage(message, type, null);
    }

    private void logToSlack(String message, LogType type, Object body) {
        String full = "*" + makeLogTypeString(type) +
                "* Environment: *" + EnvironmentSettings.getEnvironment().toUpperCase() +
                "* | Database: *" + DataSettings.getDatabase().toUpperCase() + "*\n" +
                "Message: " + message + "\n" +
            (body != null ? "Body: `" + toJson(body) + "`" : "");
        sendToSlack(full);
    }

    private void logToSlack(String message, LogType type) {
        logToSlack(message, type, null);
    }

    private void sendToSlack(String message) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            String url = "https://hooks.slack.com/services/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";
            message = message.replace("\"", "\\\"");
            String body = "{\"text\":\"" + message + "\"}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> value = restTemplate.postForEntity(url, entity, String.class);
            HttpStatus stih = value.getStatusCode();
        }
        catch (Exception e) {
            // e.printStackTrace();
        }
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed to serialize object";
        }
    }
}
