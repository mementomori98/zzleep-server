package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/devices")
@Api(value = "User devices api")
public class AccountDevicesAPI {

    @ApiOperation(value = "Add new device to account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added a device"),
    })
    @PostMapping
    public ResponseEntity<String> addDevice(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Device added?");
    }

    @ApiOperation(value = "Update existing user device")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a device"),
    })
    @PutMapping
    public ResponseEntity<String> updateDevice(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Device updated?");
    }

    @ApiOperation(value = "Get user devices by userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved account devices"),
    })
    @GetMapping
    public ResponseEntity<String> getAllUserDevices(@RequestParam(name = "userId") String userId)
    {
        return ResponseEntity
                .status(200)
                .body("Retrieved devices for account " + userId);
    }
}
