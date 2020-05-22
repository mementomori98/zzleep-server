package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.repositories.DeviceRepository;

import java.util.List;

// @RestController
// @RequestMapping("/devices")
// @Api(value = "Devices Controller")
public class DevicesController extends ControllerBase {

    private final DeviceRepository deviceRepository;

    public DevicesController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // @ApiOperation(value = "Add a device to the user", response = Device.class)
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully added a device"),
    // })
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody AddDeviceModel model) {
        // TODO model.deviceId = user from firebase
        if (deviceRepository.hasUser(model.getDeviceId()))
            return custom(403);
        return success(
            deviceRepository.update(model)
        );
    }

    // @ApiOperation(value = "Update existing device")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully updated device"),
    // })
    @PutMapping
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceModel model) {
        Device device = deviceRepository.getById(model.getDeviceId());
        String userId = ""; // TODO firebase
        if (device.getUserId() == null) return custom(403);
        if (!device.getUserId().equals(userId)) return custom(403);
        return success(
            deviceRepository.update(model)
        );
    }

    // @ApiOperation(value = "Get devices by userId")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully retrieved devices"),
    // })
    @GetMapping
    public ResponseEntity<List<Device>> getAllUserDevices() {
        String userId = ""; // TODO firebase
        return success(
            deviceRepository.getAllByUserId(userId)
        );
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getById(@PathVariable(value = "deviceId") String deviceId) {
        String userId = ""; // TODO firebase
        Device device = deviceRepository.getById(deviceId);
        if (device == null) return notFound();
        if (!userId.equals(device.getUserId())) return custom(403);
        return success(device);
    }
}
