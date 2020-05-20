package zzleep.core.repositories;

import zzleep.core.models.Interval;
import zzleep.core.models.IntervalReport;
import zzleep.core.models.RoomCondition;
import zzleep.core.models.SleepData;

public interface WarehouseRepository {

    SleepData getSleepData(int sleepId);
    IntervalReport getReport(String deviceId, Interval interval);
    RoomCondition getIdealRoomCondition(String deviceId);

}
