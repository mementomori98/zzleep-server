package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Sleep;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.SleepRepository;

@RestController
@RequestMapping("/api/tracking")
@Api(tags = {"Tracking"}, description = " ")
public class TrackingController extends ControllerBase {

    private final SleepRepository sleepRepository;
    private final AuthorizationService authService;

    public TrackingController(SleepRepository sleepRepository, AuthorizationService authService) {
        this.sleepRepository = sleepRepository;
        this.authService = authService;
    }

    @ApiOperation(value = "Start sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
        @ApiResponse(code = 403, message = "This user does not have a device with this ID"),
        @ApiResponse(code = 409, message = "This device is already tracking")
    })
    @PostMapping("/start/{deviceId}")
    public ResponseEntity<Sleep> startTracking(@PathVariable(value = "deviceId") String deviceId) {
        if (!authService.userHasDevice(userId(), deviceId)) return forbidden();
        try {
            return success(sleepRepository.startTracking(deviceId));
        } catch (SleepRepository.SleepNotStoppedException ex) {
            return custom(409);
        } catch (SleepRepository.DeviceNotFoundException ex) {
            return notFound();
        }
    }

    @ApiOperation(value = "Stop sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
        @ApiResponse(code = 403, message = "This user does not have a device with this ID"),
        @ApiResponse(code = 409, message = "This device is not currently tracking")
    })
    @PutMapping("/stop/{deviceId}")
    public ResponseEntity<Sleep> stopTracking(@PathVariable(value = "deviceId") String deviceId) {
        if (!authService.userHasDevice(userId(), deviceId)) return forbidden();
        try {
            return success(sleepRepository.stopTracking(deviceId));
        } catch (SleepRepository.DeviceNotFoundException ex) {
            return notFound();
        } catch (SleepRepository.SleepNotStartedException ex) {
            return custom(409);
        }
    }

    @ApiOperation(value = "Rate sleep", response = Sleep.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully rated sleep"),
        @ApiResponse(code = 403, message = "This user does not have a sleep with this ID")
    })
    @PutMapping("/{sleepId}/{rating}")
    public ResponseEntity<Sleep> setRating(@PathVariable(value = "sleepId") int sleepId, @PathVariable(value = "rating") int rating) {
        Sleep sleep;
        if (!authService.userHasSleep(userId(), sleepId)) return forbidden();
        try {
            sleep = sleepRepository.rateSleep(sleepId, rating);
            return success(sleep);
        } catch (SleepRepository.SleepNotFoundException ex) {
            return notFound();
        }
    }

    @ApiOperation(value = "Get whether a device is currently tracking", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved tracking state"),
        @ApiResponse(code = 403, message = "This user does not have a devcie with this ID")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Boolean> isTracking(@PathVariable(value = "deviceId") String deviceId) {
        if (!authService.userHasDevice(userId(), deviceId)) return forbidden();
        try {
            return success(sleepRepository.isTracking(deviceId));
        } catch (SleepRepository.DeviceNotFoundException e) {
            return notFound();
        }
    }
}
