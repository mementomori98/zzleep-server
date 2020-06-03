package zzleep.core.repositories;

import zzleep.core.models.Sleep;

public interface SleepRepository {
    Sleep rateSleep(int sleepId, int rating) throws SleepNotFoundException;
    Sleep startTracking(String deviceId) throws SleepNotStoppedException, DeviceNotFoundException;
    Sleep stopTracking(String deviceId) throws SleepNotStartedException, DeviceNotFoundException;
    Sleep getActiveSleep(String deviceId);
    Sleep getLatestSleep(String deviceId);
    boolean isTracking(String deviceId);

    class SleepNotStoppedException extends RuntimeException{}
    class SleepNotStartedException extends RuntimeException{}
    class SleepNotFoundException extends RuntimeException{}
    class DeviceNotFoundException extends RuntimeException{}
}
