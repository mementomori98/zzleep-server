package zzleep.communicator.controller;

import zzleep.communicator.models.Command;

import java.util.ArrayList;

public interface CommandsHandler {
    ArrayList<Command> getUpdates();
}
