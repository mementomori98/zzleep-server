package zzleep.communicator.network.fakeNetwork;

import com.google.gson.Gson;
import zzleep.communicator.models.DownLinkMessage;
import zzleep.communicator.network.WebSocketHandler;
import zzleep.communicator.network.WebSocketImpl;
import zzleep.core.logging.Logger;

import java.util.function.Consumer;

public class Proxy implements WebSocketHandler {

    private WebSocketHandler webSocket;
    private FakeDeviceSimulator simulator;
    private final Gson gson = new Gson();

    public Proxy(Consumer<String> listener, Logger logger) {
        this.webSocket = new WebSocketImpl(listener, logger);
        this.simulator = new FakeDeviceSimulatorImpl(listener);
    }

    @Override
    public void send(CharSequence data) {
        String s = data.toString();
        DownLinkMessage message = gson.fromJson(s, DownLinkMessage.class);
        if (message.getEUI().equals("0004A30B002181EC")) {
            webSocket.send(data);
        } else {
            simulator.simulate(message);
        }
    }
}
