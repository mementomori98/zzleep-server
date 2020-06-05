package zzleep.communicator.repository;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import zzleep.core.models.Sleep;

import java.util.ArrayList;
import java.util.List;


public interface PersistenceRepository {

    void insertCurrentData(CurrentData data);
    void deleteVentilation(String deviceId);
    void insertVentilation(String deviceId);
    String getDeviceIdFromActiveVentilations(String deviceId);
    int getCountOfLatestGoodValues(int sleepId);
    List<Sleep> getActiveSleepsWhereRegulationIsEnabled();
    List<Sleep> getFinishedSleeps();
    void removeActiveSleep(int sleepId);
    List<Sleep> getNewSleeps();
    void insertActiveSleep(int sleepId);

}
