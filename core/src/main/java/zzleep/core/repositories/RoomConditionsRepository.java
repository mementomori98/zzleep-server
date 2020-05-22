package zzleep.core.repositories;

import zzleep.core.models.RoomCondition;

public interface RoomConditionsRepository {
    RoomCondition getCurrentData(String deviceId) throws SleepNotFoundException, NoDataException;


    class SleepNotFoundException extends Exception{}
    class NoDataException extends Exception{}
}
