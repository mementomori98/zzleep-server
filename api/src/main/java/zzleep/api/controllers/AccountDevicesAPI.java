package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;

@RestController
@RequestMapping("/devices")
@Api(value = "User devices api")
public class AccountDevicesAPI {

    @ApiOperation(value = "Add new device to account", response = Device.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added a device"),
    })
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody AddDeviceModel model)
    {
        return ResponseEntity
                .status(200)
                .body(new Device(1234, "kitchen?"));
    }

    @ApiOperation(value = "Update existing user device")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a device"),
    })
    @PutMapping
    public ResponseEntity<String> updateDevice(@RequestBody UpdateDeviceModel model)
    {
        return ResponseEntity
                .status(200)
                .body("");
    }

    @ApiOperation(value = "Get user devices by userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved account devices"),
    })
    @GetMapping
    public ResponseEntity<Device> getAllUserDevices(@RequestParam(name = "userId") int userId)
    {
        return ResponseEntity
                .status(200)
                .body(new Device(userId, "kitchen?"));
    }
}
