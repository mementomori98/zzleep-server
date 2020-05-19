package models;

public class CurrentData {

    private double co2Data;
    private double temperatureData;
    private double humidityData;
    private double soundData;
    private String source;
    private String timeStamp;


    public CurrentData()
    {
        this.co2Data = 0;
        this.temperatureData = 0;
        this.humidityData = 0;
        this.soundData = 0;
        this.source = "";
        this.timeStamp = "";
    }

    public CurrentData(double co2Data, double temperatureData, double humidityData, double soundData, String source, String timeStamp) {
        this.co2Data = co2Data;
        this.temperatureData = temperatureData;
        this.humidityData = humidityData;
        this.soundData = soundData;
        this.source = source;
        this.timeStamp = timeStamp;
    }

    public double getCo2Data() {
        return co2Data;
    }

    public void setCo2Data(double co2Data) {
        this.co2Data = co2Data;
    }

    public double getTemperatureData() {
        return temperatureData;
    }

    public void setTemperatureData(double temperatureData) {
        this.temperatureData = temperatureData;
    }

    public double getHumidityData() {
        return humidityData;
    }

    public void setHumidityData(double humidityData) {
        this.humidityData = humidityData;
    }

    public double getSoundData() {
        return soundData;
    }

    public void setSoundData(double soundData) {
        this.soundData = soundData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "CurrentData{" +
                "co2Data=" + co2Data +
                ", temperatureData=" + temperatureData +
                ", humidityData=" + humidityData +
                ", soundData=" + soundData +
                ", source='" + source + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
