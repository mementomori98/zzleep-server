package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.RemoveDeviceModel;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.repositories.DeviceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Api(tags = {"Devices"}, description = " ")
public class DevicesController extends ControllerBase {

    private final DeviceRepository deviceRepository;

    public DevicesController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @ApiOperation(value = "Connect a device to a user", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully connected device"),
        @ApiResponse(code = 403, message = "This device is already associated with another user"),
        @ApiResponse(code = 406, message = "This device is already associated with this user")
    })
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody AddDeviceModel model) {
        // TODO firebase user id
        if (model.getUserId().equals(deviceRepository.getById(model.getDeviceId()).getUserId()))
            return custom(406);
        if (deviceRepository.hasUser(model.getDeviceId()))
            return custom(403);
        return success(
            deviceRepository.update(model)
        );
    }

    @ApiOperation(value = "Update existing device")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated device"),
        @ApiResponse(code = 403, message = "This device does not belong to this user"),
    })
    @PatchMapping
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceModel model) {
        Device device = deviceRepository.getById(model.getDeviceId());
        String userId = "user1"; // TODO firebase
        if (device.getUserId() == null) return custom(403);
        if (!device.getUserId().equals(userId)) return custom(403);
        return success(
            deviceRepository.update(model)
        );
    }

    @ApiOperation(value = "Get devices by userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved devices"),
    })
    @GetMapping
    public ResponseEntity<List<Device>> getAllUserDevices() {
        String userId = "user1"; // TODO firebase
        return success(
            deviceRepository.getAllByUserId(userId)
        );
    }

    @ApiOperation(value = "Get a device")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved device"),
        @ApiResponse(code = 403, message = "The device is not connected to this user"),
        @ApiResponse(code = 404, message = "The device was not found")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getById(@PathVariable(value = "deviceId") String deviceId) {
        String userId = "user1"; // TODO firebase
        Device device = deviceRepository.getById(deviceId);
        if (device == null) return notFound();
        if (!userId.equals(device.getUserId())) return custom(403);
        return success(device);
    }

    @ApiOperation(value = "Remove a device from user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The device has been removed from the user"),
        @ApiResponse(code = 403, message = "The device is not associated with this user"),
        @ApiResponse(code = 404, message = "The device was not found")
    })
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> remove(@PathVariable(value = "deviceId") String deviceId) {
        String userId = "user1"; // TODO firebase
        Device device = deviceRepository.getById(deviceId);
        if (device == null) return notFound();
        if (!userId.equals(device.getUserId())) return custom(403);
        deviceRepository.update(new RemoveDeviceModel(deviceId));
        return success();
    }
}
