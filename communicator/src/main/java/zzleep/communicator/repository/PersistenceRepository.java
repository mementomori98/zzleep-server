package zzleep.communicator.repository;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;
import zzleep.core.models.Sleep;

import java.util.ArrayList;
import java.util.List;


public interface PersistenceRepository {
    void putDataInDatabase(CurrentData data) ;
    void deleteVentilationFromDb(String deviceId);
    void insertVentilationInDb(String deviceId);
    String getDeviceIdFromActiveVentilations(String deviceId);
    int getCountOfLatestGoodValues(int sleepId);
    List<Sleep> getActiveSleepsWhereRegulationIsEnabled();
    List<Sleep> getFinishedSleeps();
    void removeActiveSleep(int sleepId);
    List<Sleep> getNewSleeps();
    void insertInActiveSleeps(int sleepId);
    //ArrayList<Command> getUpdates();
}
