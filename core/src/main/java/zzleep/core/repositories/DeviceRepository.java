package zzleep.core.repositories;

import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.UpdateDeviceModel;

import java.util.List;

public interface DeviceRepository {

    Device update(AddDeviceModel model);
    Device update(UpdateDeviceModel model);
    List<Device> getAllByUserId(String userId);
    Device getById(String deviceId);
    boolean hasUser(String deviceId);

}
