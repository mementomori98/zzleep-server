package zzleep.core.repositories;

import zzleep.core.models.*;

public interface WarehouseRepository {

    SleepData getSleepData(int sleepId);
    IntervalReport getReport(String deviceId, Interval interval);
    IdealRoomConditions getIdealRoomCondition(String deviceId);

}
