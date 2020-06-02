package zzleep.core.models;

import java.time.LocalDate;

public class GetIntervalReportModel {

    private String deviceId;
    private final LocalDate start;
    private final LocalDate end;

    public GetIntervalReportModel(String deviceId, LocalDate start, LocalDate end) {
        this.deviceId = deviceId;
        this.start = start;
        this.end = end;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
