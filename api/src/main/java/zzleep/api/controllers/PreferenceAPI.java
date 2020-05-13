package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Preferences;
import zzleep.core.models.SetPreferencesModel;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/preferences")
@Api(value = "Preference api")
public class PreferenceAPI {

    @ApiOperation(value = "Get current preferences", response = Preferences.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
    })
    @GetMapping
    public ResponseEntity<Preferences> getPreferences(@RequestParam(name = "devId") int deviceId)
    {
        return ResponseEntity
                .status(200)
                .body(new Preferences(deviceId, true, 10, 10, 10, 10.0, 10.0));
    }

    @ApiOperation(value = "Update user preferences")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated preferences"),
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    @PutMapping
    public ResponseEntity updateAccount(@RequestBody SetPreferencesModel model)
    {
        return ResponseEntity.status(200).body("");
    }
}
