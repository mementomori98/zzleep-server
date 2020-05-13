package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;
import zzleep.core.repositories.SleepRepository;
import zzleep.core.repositories.TestRepository;

@RestController
@RequestMapping("/sleep")
@Api(value = "User sleep api")
public class SleepController {

    private final SleepRepository sleepRepository;

    public SleepController(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    @RequestMapping("/startTracking/{deviceId}")
    @ApiOperation(value = "Start sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PostMapping
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

    @RequestMapping("/stopTracking/{deviceId}")
    @ApiOperation(value = "Stop sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PutMapping
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

    @RequestMapping("/{sleepId}/{rating}")
    @ApiOperation(value = "Stop sleep tracking", response = Sleep.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started tracking the room conditions"),
    })
    @PutMapping
    public ResponseEntity<Sleep> setRating(@PathVariable(value="sleepId") String sleepid, @PathVariable(value="rating") int rating)
    {
        Sleep sleep;
        try {
            sleep = sleepRepository.rateSleep(sleepid, rating);
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
}
