package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/preferences")
@Api(value = "Preference api")
public class PreferenceAPI {

    @ApiOperation(value = "Get current preferences")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
    })
    @GetMapping
    public ResponseEntity<String> getPreferences(@RequestParam(name = "devId") String deviceId)
    {
        return ResponseEntity
                .status(200)
                .body("Preferences retrieved for device with id:" + deviceId);
    }

    @ApiOperation(value = "Update user preferences")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated preferences"),
    })
    @PutMapping
    public ResponseEntity<String> updateAccount(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Account preferences updated?");
    }
}
