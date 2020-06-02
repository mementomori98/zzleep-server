package zzleep.core.models;

public class RemoveDeviceModel {

    private String deviceId;

    public RemoveDeviceModel(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RemoveDeviceModel))
            return false;
        return ((RemoveDeviceModel) obj).deviceId.equals(deviceId);
    }
}
