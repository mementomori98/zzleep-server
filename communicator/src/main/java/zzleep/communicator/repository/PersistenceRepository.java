package zzleep.communicator.repository;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;

import java.util.ArrayList;


public interface PersistenceRepository {
    void putDataInDatabase(CurrentData data) ;
    ArrayList<Command> getUpdates();
}
