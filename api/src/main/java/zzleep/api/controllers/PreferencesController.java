package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.Preferences;
import zzleep.core.models.SetPreferencesModel;
import zzleep.core.repositories.PreferencesRepository;

@RestController
@RequestMapping("/api/preferences")
@Api(value = "Preferences api")
public class PreferencesController {
    private final PreferencesRepository preferencesRepository;

    public PreferencesController(PreferencesRepository preferencesRepository) {
        this.preferencesRepository = preferencesRepository;
    }

    @ApiOperation(value = "Get current preferences", response = Preferences.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
            @ApiResponse(code = 404, message = "Something went wrong")
    })
    @GetMapping
    public ResponseEntity<Preferences> getPreferences(@RequestParam(name = "devId") String deviceId)
    {
        Preferences pref = preferencesRepository.getPreferences(deviceId);
        if(pref != null)
            return ResponseEntity
                .status(200)
                .body(pref);
        else return ResponseEntity.status(404).body(null);
    }

    @ApiOperation(value = "Update user preferences")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated preferences")
    })
    @PutMapping
    public ResponseEntity<Preferences> updateAccount(@RequestBody Preferences model)
    {
        Preferences pref = preferencesRepository.setPreferences(model);
        return ResponseEntity.status(200).body(pref);
    }
}
