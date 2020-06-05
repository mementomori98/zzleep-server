package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.AddDeviceModel;
import zzleep.core.models.Device;
import zzleep.core.models.UpdateDeviceModel;
import zzleep.core.services.Authorized;
import zzleep.core.services.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Api(tags = {"Devices"}, description = " ")
public class DevicesController extends ControllerBase {

    private final DeviceService deviceService;

    public DevicesController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @ApiOperation(value = "Connect a device to a user", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully connected device"),
        @ApiResponse(code = 403, message = "This device is already associated with another user"),
        @ApiResponse(code = 406, message = "This device is already associated with this user")
    })
    @PostMapping
    public ResponseEntity<Device> addDevice(
        @RequestBody AddDeviceModel model
    ) {
        return map(
            deviceService.add(new Authorized<>(userId(), model))
        );
    }

    @ApiOperation(value = "Update existing device", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated device"),
        @ApiResponse(code = 403, message = "This device does not belong to this user"),
    })
    @PatchMapping
    public ResponseEntity<Device> updateDevice(
        @RequestBody UpdateDeviceModel model
    ) {
        return map(
            deviceService.update(new Authorized<>(userId(), model))
        );
    }

    @ApiOperation(value = "Get devices by userId", response = Device[].class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved devices"),
    })
    @GetMapping
    public ResponseEntity<List<Device>> getAllUserDevices() {
        return map(
            deviceService.getAllByUser(new Authorized<>(userId()))
        );
    }

    @ApiOperation(value = "Get a device", response = Device.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved device"),
        @ApiResponse(code = 403, message = "The device is not connected to this user")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getById(
        @PathVariable(value = "deviceId") String deviceId
    ) {
        return map(
            deviceService.getById(new Authorized<>(userId(), deviceId))
        );
    }

    @ApiOperation(value = "Remove a device from user", response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The device has been removed from the user"),
        @ApiResponse(code = 403, message = "The device is not associated with this user")
    })
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> remove(
        @PathVariable(value = "deviceId") String deviceId
    ) {
        return map(
            deviceService.remove(new Authorized<>(userId(), deviceId))
        );
    }

    @ApiOperation(value = "[DEV] Get all available device IDs")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The list of available device IDs have been retrieved")
    })
    @GetMapping("/available")
    public ResponseEntity<List<String>> getAvailableDevices() {
        return map(
            deviceService.getAllAvailable()
        );
    }
}
