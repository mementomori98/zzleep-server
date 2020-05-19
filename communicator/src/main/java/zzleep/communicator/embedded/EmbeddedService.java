package zzleep.communicator.embedded;


import models.CurrentData;

import java.net.http.WebSocket;

public interface EmbeddedService extends Runnable{
    void receive(CurrentData data);

}
