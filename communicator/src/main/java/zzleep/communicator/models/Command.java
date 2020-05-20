package zzleep.communicator.models;

public class Command {

    private String destination;
    private char commandID;  //D - Device; V - Ventilation
    private int value; //1 - ON; 0 - OFF

    public Command()
    {
        this.destination = "";
        this.commandID = ' ';
        this.value = 0;
    }

    public Command(String destination, char commandID, int value) {
        this.destination = destination;
        this.commandID = commandID;
        this.value = value;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public char getCommandID() {
        return commandID;
    }

    public void setCommandID(char commandID) {
        this.commandID = commandID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Command{" +
                "destination='" + destination + '\'' +
                ", commandID=" + commandID +
                ", value=" + value +
                '}';
    }
}
