package zzleep.communicator.controller.commandProtocolHandler;

import zzleep.communicator.models.Command;

public interface SendCommandProtocolHandler {

    boolean shouldSend(Command command);
    boolean hasPendingCommand();
    Command getPendingCommand();
}
