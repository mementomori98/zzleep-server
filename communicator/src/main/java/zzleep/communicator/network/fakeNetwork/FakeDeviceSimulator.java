package zzleep.communicator.network.fakeNetwork;

import zzleep.communicator.models.DownLinkMessage;

public interface FakeDeviceSimulator {

    void simulate(DownLinkMessage message);

}
