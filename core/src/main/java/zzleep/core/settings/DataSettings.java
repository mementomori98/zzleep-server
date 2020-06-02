package zzleep.core.settings;

import org.springframework.boot.logging.logback.LogbackLoggingSystem;

public class DataSettings {

    private static String database;
    private static String url;
    private static String user;
    private static String password;

    static {
        String env = System.getenv("_env");
        if (env == null) env = "prod";
        database = env;
        switch (env) {
            case "prod":
                configureProd();
                break;
            default:
                configureProd();
        }
    }

    private static void configureProd() {
        url = "jdbc:postgresql://ec2-54-217-213-79.eu-west-1.compute.amazonaws.com:5432/d5agr6187gipnp";
        user = "vukflngbflapxv";
        password = "8f2399a5934e3dbc54e6b6f2713bc57bc8176a52d27f63f60f7b48885d22d3a6";
    }

    public static String getDatabase() {
        return database;
    }

    public static String getUrl() {
        return url;
    }

    public static void Shit() {

    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

}
