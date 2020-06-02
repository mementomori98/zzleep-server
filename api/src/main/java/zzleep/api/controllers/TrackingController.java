package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Sleep;
import zzleep.core.models.SleepRating;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.SleepRepository;
import zzleep.core.services.Authorized;
import zzleep.core.services.TrackingService;

@RestController
@RequestMapping("/api/tracking")
@Api(tags = {"Tracking"}, description = " ")
public class TrackingController extends ControllerBase {


    private final TrackingService trackingService;

    public TrackingController( TrackingService trackingService) {

        this.trackingService = trackingService;
    }

    @ApiOperation(value = "Start sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
        @ApiResponse(code = 403, message = "This user does not have a device with this ID"),
        @ApiResponse(code = 409, message = "This device is already tracking")
    })
    @PostMapping("/start/{deviceId}")
    public ResponseEntity<Sleep> startTracking(@PathVariable(value = "deviceId") String deviceId) {
       return  map(trackingService.startTracking(new Authorized<>(userId(), deviceId)));
    }

    @ApiOperation(value = "Stop sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
        @ApiResponse(code = 403, message = "This user does not have a device with this ID"),
        @ApiResponse(code = 409, message = "This device is not currently tracking")
    })
    @PutMapping("/stop/{deviceId}")
    public ResponseEntity<Sleep> stopTracking(@PathVariable(value = "deviceId") String deviceId) {
      return map(trackingService.stopTracking(new Authorized<>(userId(), deviceId)));
    }

    @ApiOperation(value = "Rate sleep", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully rated sleep"),
        @ApiResponse(code = 403, message = "This user does not have a sleep with this ID")
    })
    @PutMapping("/{sleepId}/{rating}")
    public ResponseEntity<Sleep> setRating(@PathVariable(value = "sleepId") int sleepId, @PathVariable(value = "rating") int rating) {

        return map(trackingService.setRating(new Authorized<>(userId(), new SleepRating(sleepId, rating))));
    }

    @ApiOperation(value = "Get whether a device is currently tracking", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved tracking state"),
        @ApiResponse(code = 403, message = "This user does not have a devcie with this ID")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Boolean> isTracking(@PathVariable(value = "deviceId") String deviceId) {
        return map(trackingService.isTracking(new Authorized<>(userId(), deviceId)));
    }
}
