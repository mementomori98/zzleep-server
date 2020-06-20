package zzleep.communicator.controller;

import org.springframework.stereotype.Component;
import zzleep.communicator.models.Command;
import zzleep.communicator.repository.PersistenceRepository;
import zzleep.core.models.Sleep;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandsServiceImpl implements CommandsService {
    private PersistenceRepository repository;

    public CommandsServiceImpl(PersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArrayList<Command> getUpdates() {
        return mergeCommandsInOne(getActiveSleepCommands(), getStoppedSleepCommands(), getVentilationCommands());
    }

    public ArrayList<Command> mergeCommandsInOne(ArrayList<Command> activeSleepCommands, ArrayList<Command> stoppedSleepCommands, ArrayList<Command> ventilationCommands) {
        activeSleepCommands.addAll(stoppedSleepCommands);
        activeSleepCommands.addAll(ventilationCommands);
        return activeSleepCommands;
    }

    public ArrayList<Command> getActiveSleepCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        List<Sleep> startDevices = getActiveSleeps();
        for (Sleep source : startDevices) {
            Command command = new Command(source.getDeviceId(), 'D', 1);
            commands.add(command);
        }
        return commands;
    }

    public ArrayList<Command> getStoppedSleepCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        List<Sleep> stopDevices = getStoppedSleeps();
        for (Sleep source : stopDevices) {
            Command command = new Command(source.getDeviceId(), 'D', 0);
            commands.add(command);
        }
        ArrayList<Command> stoppedSleepsVentilationCommands = stopVentilationForStoppedSleeps(stopDevices);
        commands.addAll(stoppedSleepsVentilationCommands);
        return commands;
    }

    public ArrayList<Command> stopVentilationForStoppedSleeps(List<Sleep> stopDevicesForSleeps) {
        ArrayList<Command> commands = new ArrayList<>();
        for (int i = 0; i < stopDevicesForSleeps.size(); ++i) {
            if (repository.getDeviceIdFromActiveVentilations(stopDevicesForSleeps.get(i).getDeviceId()) != null) {
                repository.deleteVentilationFromDb(stopDevicesForSleeps.get(i).getDeviceId());
                commands.add(new Command(stopDevicesForSleeps.get(i).getDeviceId(), 'V', 0));
            }
        }
        return commands;
    }

    public ArrayList<Command> getVentilationCommands() {
        ArrayList<Command> commands = new ArrayList<>();
        List<Sleep> sleeps = repository.getActiveSleepsWhereRegulationIsEnabled();

        for (int i = 0; i < sleeps.size(); ++i) {
            Command c = createCommand(sleeps.get(i), repository.getCountOfLatestGoodValues(sleeps.get(i).getSleepId()));
            if (c != null)
                commands.add(c);
        }
        return commands;
    }

    public Command createCommand(Sleep sleep, int count) {
        if (count < 3) {
            return getStartVentilationCommand(sleep);
        } else {
            return getStopVentilationCommand(sleep);
        }
    }

    public Command getStartVentilationCommand(Sleep sleep) {
        if (!isSleepAlreadyRegulated(sleep)) {
            repository.insertVentilationInDb(sleep.getDeviceId());
            return new Command(sleep.getDeviceId(), 'V', 1);
        }
        return null;
    }

    public Command getStopVentilationCommand(Sleep sleep) {
        if (isSleepAlreadyRegulated(sleep)) {
            repository.deleteVentilationFromDb(sleep.getDeviceId());
            return new Command(sleep.getDeviceId(), 'V', 0);
        }
        return null;
    }

    public boolean isSleepAlreadyRegulated(Sleep sleep) {
        String deviceId = repository.getDeviceIdFromActiveVentilations(sleep.getDeviceId());
        return !(deviceId == null);
    }

    public List<Sleep> getStoppedSleeps() {

        List<Sleep> sleeps = repository.getFinishedSleeps();
        removeFromActiveSleeps(sleeps);
        return sleeps;
    }

    public void removeFromActiveSleeps(List<Sleep> sleeps) {
        for (int i = 0; i < sleeps.size(); ++i)
            repository.removeActiveSleep(sleeps.get(i).getSleepId());
    }

    public List<Sleep> getActiveSleeps() {
        List<Sleep> newSleeps = repository.getNewSleeps();
        insertNewSleepsInActiveSleeps(newSleeps);
        return newSleeps;
    }

    public void insertNewSleepsInActiveSleeps(List<Sleep> sleeps) {
        for (int i = 0; i < sleeps.size(); ++i) {
            repository.insertInActiveSleeps(sleeps.get(i).getSleepId());
        }
    }
}
