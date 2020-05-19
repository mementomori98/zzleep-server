package zzleep.communicator.databaseService;

import models.Command;
import models.CurrentData;

import java.util.ArrayList;


public interface DatabaseService {
    void putDataInDatabase(CurrentData data);
    ArrayList<Command> getUpdates();
}
