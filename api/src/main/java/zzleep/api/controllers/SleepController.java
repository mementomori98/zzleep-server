package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;

@RestController
@RequestMapping("/sleep")
@Api(value = "User sleep api")
public class SleepController {



   /* @RequestMapping("/startTracking/{deviceId}")
    @ApiOperation(value = "Start sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PostMapping
    public ResponseEntity<Sleep> addSleep(@PathVariable(value="deviceId") String deviceId)
    {

        return ResponseEntity
                .status(200)
                .body(new Sleep(1234, new );
    }

    @RequestMapping("/startTracking/{deviceId}");
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
    }*/
}
