package zzleep.communicator.embedded;


import zzleep.communicator.models.CurrentData;
import zzleep.core.repositories.SleepRepository;

public interface EmbeddedService extends Runnable{
    void receive(CurrentData data) ;

}
