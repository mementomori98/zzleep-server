package zzleep.communicator.controller.commandProtocolHandler;

import org.springframework.stereotype.Component;
import zzleep.communicator.models.Command;


@Component
public class SendCommandProtocolHandlerImpl implements SendCommandProtocolHandler {

    private Command pendingCommand;


    public SendCommandProtocolHandlerImpl() {
        this.pendingCommand = null;
    }


    @Override
    public boolean shouldSend(Command command) {
        if (isForFakeDevice(command)) return true;
        return false;
    }

    @Override
    public boolean hasPendingCommand() {
        if (pendingCommand == null) return false;
        return true;
    }


    @Override
    public Command getPendingCommand() {
        Command command = pendingCommand;
        pendingCommand = null;
        return command;
    }

    public void setPendingCommand(Command pendingCommand) {
        this.pendingCommand = pendingCommand;
    }

    private boolean isForFakeDevice(Command command) {
        if (command.getDestination().equals("0004A30B002181EC")) {
            setPendingCommand(command);
            return false;
        }

        return true;
    }
}
