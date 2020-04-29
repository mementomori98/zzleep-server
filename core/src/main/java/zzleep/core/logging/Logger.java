package zzleep.core.logging;

public interface Logger {

    void log(String message, LogType type);
    void log(String message, LogType type, Object body);
    void info(String message);
    void info(String message, Object body);
    void warn(String message);
    void warn(String message, Object body);
    void error(String message);
    void error(String message, Object body);

}
