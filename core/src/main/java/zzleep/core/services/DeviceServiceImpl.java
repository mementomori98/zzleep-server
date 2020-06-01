package zzleep.core.services;

import org.springframework.stereotype.Component;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.RemoveDeviceModel;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.DeviceRepository;

import java.util.List;

@Component
public class DeviceServiceImpl extends ServiceBase implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final AuthorizationService authService;

    public DeviceServiceImpl(
        DeviceRepository deviceRepository,
        AuthorizationService authService
    ) {
        this.deviceRepository = deviceRepository;
        this.authService = authService;
    }

    @Override
    public Response<Device> add(Authorized<AddDeviceModel> request) {
        AddDeviceModel model = request.getModel();
        model.setUserId(request.getUserId());
        Device device = deviceRepository.getById(model.getDeviceId());

        if (device == null) return notFound();
        if (authService.userHasDevice(request.getUserId(), model.getDeviceId())) return notAllowed();
        if (deviceRepository.hasUser(model.getDeviceId())) return unauthorized();

        return success(
            deviceRepository.update(model)
        );
    }

    @Override
    public Response<Device> update(Authorized<UpdateDeviceModel> request) {
        UpdateDeviceModel model = request.getModel();

        if (!deviceRepository.exists(model.getDeviceId())) return notFound();
        if (!authService.userHasDevice(request.getUserId(), model.getDeviceId())) return unauthorized();

        return success(
            deviceRepository.update(model)
        );
    }

    @Override
    public Response<List<Device>> getAllByUser(Authorized<Void> request) {
        return success(
            deviceRepository.getAllByUserId(request.getUserId())
        );
    }

    @Override
    public Response<Device> getById(Authorized<String> request) {
        String deviceId = request.getModel();

        if (!deviceRepository.exists(deviceId)) return notFound();
        if (!authService.userHasDevice(request.getUserId(), deviceId)) return unauthorized();

        return success(
            deviceRepository.getById(deviceId)
        );
    }

    @Override
    public Response<Void> remove(Authorized<String> request) {
        String deviceId = request.getModel();

        if (!deviceRepository.exists(deviceId)) return notFound();
        if (!authService.userHasDevice(request.getUserId(), deviceId)) return unauthorized();

        deviceRepository.update(new RemoveDeviceModel(deviceId));
        return success();
    }

    @Override
    public Response<List<String>> getAllAvailable() {
        return success(
            deviceRepository.getAllAvailableIds()
        );
    }
}
