package zzleep.core.repositories;

import zzleep.core.models.RoomCondition;

public interface RoomConditionsRepository {

    RoomCondition getCurrent(String deviceId);
    RoomCondition getLatest(String deviceId);

    class SleepNotFoundException extends RuntimeException{}
    class NoDataException extends RuntimeException{}
}
