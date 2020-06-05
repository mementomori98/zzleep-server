package zzleep.communicator.controller;


import zzleep.communicator.models.Command;
import zzleep.communicator.models.CurrentData;

public interface EmbeddedController extends Runnable{
    void receive(CurrentData data) ;


}
