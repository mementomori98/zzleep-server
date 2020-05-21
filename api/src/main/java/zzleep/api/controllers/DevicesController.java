package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @RestController
// @RequestMapping("/devices")
// @Api(value = "Devices Controller")
public class DevicesController extends ControllerBase {

    // @ApiOperation(value = "Add a device to the user", response = Device.class)
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully added a device"),
    // })
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody AddDeviceModel model)
    {
        return success(new Device(model.getDeviceId(), model.getName()));
    }

    // @ApiOperation(value = "Update existing device")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully updated device"),
    // })
    @PutMapping
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceModel model)
    {
        return success(new Device(model.getDeviceId(), model.getName()));
    }

    // @ApiOperation(value = "Get devices by userId")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully retrieved devices"),
    // })
    @GetMapping
    public ResponseEntity<List<Device>> getAllUserDevices(@RequestParam(name = "userId") String userId)
    {
        return success(Arrays.asList(new Device(userId, "kitchen?")));
    }
}
