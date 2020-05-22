package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;
import zzleep.core.repositories.SleepRepository;

@RestController
@RequestMapping("/api/tracking")
@Api(tags = {"Tracking"}, description = " ")
public class TrackingController extends ControllerBase {

    private final SleepRepository sleepRepository;

    public TrackingController(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    @ApiOperation(value = "Start sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PostMapping("/start/{deviceId}")
    public ResponseEntity<Sleep> startTracking(@PathVariable(value="deviceId") String deviceId)
    {
        Sleep sleep;
        try {
            sleep = sleepRepository.startTracking(deviceId);
            return ResponseEntity
                    .status(200)
                    .body(sleep);
        }catch(SleepRepository.SleepNotStoppedException ex)
        {
            return ResponseEntity
                    .status(409)
                    .body(null);
        }
    }

    @ApiOperation(value = "Stop sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PutMapping("/stop/{deviceId}")
    public ResponseEntity<Sleep> stopTracking(@PathVariable(value="deviceId") String deviceId)
    {
        Sleep sleep;
        try {
            sleep = sleepRepository.stopTracking(deviceId);
            return ResponseEntity
                    .status(200)
                    .body(sleep);
        }catch(SleepRepository.SleepNotStartedException ex)
        {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }

    @ApiOperation(value = "Rate sleep", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully rated sleep"),
    })
    @PutMapping("/{sleepId}/{rating}")
    public ResponseEntity<Sleep> setRating(@PathVariable(value="sleepId") String sleepId, @PathVariable(value="rating") int rating)
    {
        Sleep sleep;
        try {
            sleep = sleepRepository.rateSleep(sleepId, rating);
            return ResponseEntity
                    .status(200)
                    .body(sleep);
        }catch(SleepRepository.SleepNotFoundException ex)
        {
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Boolean> isTracking(@PathVariable(value = "deviceId") String deviceId) {
        return success(sleepRepository.isTracking(deviceId));
    }
}
