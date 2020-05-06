package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/condition")
@Api(value = "Room condition api")
public class RoomConditionAPI {

    @ApiOperation(value = "Get current room condition")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved current room condition"),
    })
    @GetMapping
    public ResponseEntity<String> getReport(@RequestParam(name = "devId") String deviceId)
    {
        return ResponseEntity
                .status(200)
                .body("A room condition for device with id #" + deviceId);
    }
}
