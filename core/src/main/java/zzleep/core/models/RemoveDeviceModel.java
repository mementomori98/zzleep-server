package zzleep.core.models;

public class RemoveDeviceModel {

    private String deviceId;

    public RemoveDeviceModel(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
