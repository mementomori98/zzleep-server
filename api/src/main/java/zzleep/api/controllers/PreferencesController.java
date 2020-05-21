package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Preferences;
import zzleep.core.models.SetPreferencesModel;

// @RestController
// @RequestMapping("/preferences")
// @Api(value = "Preferences api")
public class PreferencesController {

    // @ApiOperation(value = "Get current preferences", response = Preferences.class)
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
    // })
    @GetMapping
    public ResponseEntity<Preferences> getPreferences(@RequestParam(name = "devId") int deviceId)
    {
        return ResponseEntity
                .status(200)
                .body(new Preferences(deviceId, true, 10, 10, 10, 10.0, 10.0));
    }

    // @ApiOperation(value = "Update user preferences")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully updated preferences"),
    //         @ApiResponse(code = 400, message = "Something went wrong")
    // })
    @PutMapping
    public ResponseEntity updateAccount(@RequestBody SetPreferencesModel model)
    {
        return ResponseEntity.status(200).body("");
    }
}
