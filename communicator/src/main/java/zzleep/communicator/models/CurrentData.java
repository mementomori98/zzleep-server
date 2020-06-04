package zzleep.communicator.models;

public class CurrentData {

    private Integer co2Data;
    private Integer temperatureData;
    private Double humidityData;
    private Double soundData;
    private String source;
    private String timeStamp;


    public CurrentData()
    {
        this.co2Data = 0;
        this.temperatureData = 0;
        this.humidityData = 0.0;
        this.soundData = 0.0;
        this.source = "";
        this.timeStamp = "";
    }

    public CurrentData(int co2Data, int temperatureData, double humidityData, double soundData, String source, String timeStamp) {
        this.co2Data = co2Data;
        this.temperatureData = temperatureData;
        this.humidityData = humidityData;
        this.soundData = soundData;
        this.source = source;
        this.timeStamp = timeStamp;
    }

    public Integer getCo2Data() {
        return co2Data;
    }

    public void setCo2Data(Integer co2Data) {
        this.co2Data = co2Data;
    }

    public Integer getTemperatureData() {
        return temperatureData;
    }

    public void setTemperatureData(Integer temperatureData) {
        this.temperatureData = temperatureData;
    }

    public Double getHumidityData() {
        return humidityData;
    }

    public void setHumidityData(Double humidityData) {
        this.humidityData = humidityData;
    }

    public Double getSoundData() {
        return soundData;
    }

    public void setSoundData(Double soundData) {
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
