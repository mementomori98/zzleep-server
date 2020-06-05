package zzleep.core.services;

import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.UpdateDeviceModel;

import java.util.List;

public interface DeviceService {

    Response<Device> add(Authorized<AddDeviceModel> request);
    Response<Device> update(Authorized<UpdateDeviceModel> request);
    Response<List<Device>> getAllByUser(Authorized<Void> request);
    Response<Device> getById(Authorized<String> request);
    Response<Void> remove(Authorized<String> request);

    // FIXME For development purposes, should be removed for production.
    Response<List<String>> getAllAvailable();

}
