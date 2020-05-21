package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzleep.core.models.RoomCondition;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/api/room-conditions")
@Api(tags = {"Room Conditions"}, description = " ")
public class RoomConditionsController {


    @ApiOperation(value = "Get current room condition")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved current room condition"),
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<RoomCondition> getReport(@PathVariable(name = "deviceId") String deviceId) {
        Random random = new Random();
        RoomCondition dummy = new RoomCondition(
            random.nextInt(100),
            LocalDateTime.now(),
            random.nextDouble() * 15 + 15,
            random.nextDouble() * 200 + 400,
            random.nextDouble() * 50 + 20,
            random.nextDouble() * 100
        );
        return ResponseEntity
            .status(200)
            .body(dummy);
    }
}
