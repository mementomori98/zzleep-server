package zzleep.core.repositories;

import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import zzleep.core.models.Sleep;

public interface SleepRepository {
    Sleep rateSleep(String sleepId, int rating) throws SleepNotFoundException;
    Sleep startTracking(String deviceId) throws SleepNotStoppedException;
    Sleep stopTracking(String deviceId) throws SleepNotStartedException;

    class SleepNotStoppedException extends Exception{}
    class SleepNotStartedException extends Exception{}
    class SleepNotFoundException extends Exception{}
}
