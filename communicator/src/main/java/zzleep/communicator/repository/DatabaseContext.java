package zzleep.communicator.repository;

import java.util.ArrayList;

public interface DatabaseContext {
    void insert(String values);
    ArrayList<String> getActiveSleeps();
    ArrayList<String> getStoppedSleeps();
    ArrayList<String> getStartVentilation();
    ArrayList<String> getStopVentilation();
}
