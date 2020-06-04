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
import zzleep.core.services.Authorized;
import zzleep.core.services.RoomConditionsService;

@RestController
@RequestMapping("/api/room-conditions")
@Api(tags = {"Room Conditions"}, description = " ")
public class RoomConditionsController extends ControllerBase {


    private final RoomConditionsService roomConditionsService;

    public RoomConditionsController(RoomConditionsService roomConditionsService) {
        this.roomConditionsService = roomConditionsService;

    }

    @ApiOperation(value = "Get current room condition", response = RoomCondition.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved current room condition"),
        @ApiResponse(code = 204, message = "No measurements have been made during this tracking session yet"),
        @ApiResponse(code = 403, message = "The user does not have a device with this ID"),
        @ApiResponse(code = 404, message = "There is no active tracking for this device")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<RoomCondition> getCurrent(@PathVariable(name = "deviceId") String deviceId) {
        return map(roomConditionsService.getCurrent(new Authorized<>(userId(), deviceId)));
    }

    @ApiOperation(value = "Get the latest recorded room condition for a device", response = RoomCondition.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved latest recorded room condition"),
        @ApiResponse(code = 204, message = "The current tracking session is the first, and it has not measured any values yet"),
        @ApiResponse(code = 403, message = "The user does not have a device with this ID"),
        @ApiResponse(code = 404, message = "No measurements have been made on this device yet")
    })
    @GetMapping("/{deviceId}/latest")
    public ResponseEntity<RoomCondition> getLatest(@PathVariable(name = "deviceId") String deviceId) {
        return map(roomConditionsService.getLatest(new Authorized<>(userId(), deviceId)));
    }
}
