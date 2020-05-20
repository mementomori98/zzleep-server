package zzleep.communicator.embedded;


import zzleep.communicator.models.CurrentData;

public interface EmbeddedService extends Runnable{
    void receive(CurrentData data);

}
