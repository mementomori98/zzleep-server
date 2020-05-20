package zzleep.communicator.databaseService;

import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;

import java.util.ArrayList;


public interface DatabaseService {
    void putDataInDatabase(CurrentData data);
    ArrayList<Command> getUpdates();
}
