package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/sleep/{deviceId}")
@Api(value = "User sleep api")
public class SleepTrackingAPI {

    @ApiOperation(value = "Set tracking on")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking"),
    })
    @PutMapping("/on")
    public ResponseEntity<String> setTrackingOn(@PathVariable String deviceId)
    {
        return ResponseEntity
                .status(200)
                .body("Started tracking for device " + deviceId);
    }

    @ApiOperation(value = "Set tracking off")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully stoped tracking"),
    })
    @PutMapping("/off")
    public ResponseEntity<String> setTrackingOff(@PathVariable String deviceId, @RequestParam( name = "rating") int rating)
    {
        return ResponseEntity
                .status(200)
                .body("Stoped tracking for device " + deviceId + " with a rating of " + rating);
    }

    @ApiOperation(value = "Get user account by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved account sleeps"),
    })
    @GetMapping
    public ResponseEntity<String> getSleepSessions(@PathVariable String deviceId)
    {
        return ResponseEntity
                .status(200)
                .body("Retrieved sleeps for device " + deviceId);
    }
}
