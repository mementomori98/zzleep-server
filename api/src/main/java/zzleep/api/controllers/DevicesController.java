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
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.DeviceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Api(tags = {"Devices"}, description = " ")
public class DevicesController extends ControllerBase {

    private final DeviceRepository deviceRepository;
    private final AuthorizationService authService;

    public DevicesController(DeviceRepository deviceRepository, AuthorizationService authService) {
        this.deviceRepository = deviceRepository;
        this.authService = authService;
    }

    @ApiOperation(value = "Connect a device to a user", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully connected device"),
        @ApiResponse(code = 403, message = "This device is already associated with another user"),
        @ApiResponse(code = 406, message = "This device is already associated with this user")
    })
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody AddDeviceModel model) {
        model.setUserId(userId());
        Device device = deviceRepository.getById(model.getDeviceId());
        if (device == null) return notFound();
        if (authService.userHasDevice(model.getUserId(), model.getDeviceId()))
            return custom(406);
        if (deviceRepository.hasUser(model.getDeviceId()))
            return custom(403);
        return success(
            deviceRepository.update(model)
        );
    }

    @ApiOperation(value = "Update existing device", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated device"),
        @ApiResponse(code = 403, message = "This device does not belong to this user"),
    })
    @PatchMapping
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceModel model) {
        if (!deviceRepository.exists(model.getDeviceId()))
            return notFound();
        if (!authService.userHasDevice(userId(), model.getDeviceId()))
            return forbidden();
        return success(
            deviceRepository.update(model)
        );
    }

    @ApiOperation(value = "Get devices by userId", response = Device[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved devices"),
    })
    @GetMapping
    public ResponseEntity<List<Device>> getAllUserDevices() {
        return success(
            deviceRepository.getAllByUserId(userId())
        );
    }

    @ApiOperation(value = "Get a device", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved device"),
        @ApiResponse(code = 403, message = "The device is not connected to this user")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getById(@PathVariable(value = "deviceId") String deviceId) {
        if (!deviceRepository.exists(deviceId))
            return notFound();
        if (!authService.userHasDevice(userId(), deviceId))
            return forbidden();
        return success(deviceRepository.getById(deviceId));
    }

    @ApiOperation(value = "Remove a device from user", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The device has been removed from the user"),
        @ApiResponse(code = 403, message = "The device is not associated with this user")
    })
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> remove(@PathVariable(value = "deviceId") String deviceId) {
        if (!deviceRepository.exists(deviceId))
            return notFound();
        if (!authService.userHasDevice(userId(), deviceId))
            return forbidden();
        deviceRepository.update(new RemoveDeviceModel(deviceId));
        return success();
    }

    @ApiOperation(value = "[DEV] Get all available device IDs")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The list of available device IDs have been retrieved")
    })
    @GetMapping("/available")
    public ResponseEntity<List<String>> getAvailableDevices() {
        return success(deviceRepository.getAllAvailableIds());
    }
}
