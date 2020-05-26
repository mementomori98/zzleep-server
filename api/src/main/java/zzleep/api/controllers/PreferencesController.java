package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.logging.Logger;
import zzleep.core.models.Preferences;
import zzleep.core.repositories.PreferencesRepository;

@RestController
@RequestMapping("/api/preferences")
@Api(value = "Preferences api")
public class PreferencesController {
    private final PreferencesRepository preferencesRepository;
    private final Logger logger;

    public PreferencesController(PreferencesRepository preferencesRepository, Logger logger) {
        this.preferencesRepository = preferencesRepository;
        this.logger = logger;
    }

    @ApiOperation(value = "Get current preferences", response = Preferences.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
            @ApiResponse(code = 404, message = "Something went wrong")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Preferences> getPreferences(@PathVariable(name = "deviceId") String deviceId)
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
            @ApiResponse(code = 200, message = "Successfully updated preferences"),
            @ApiResponse(code = 404, message = "Inexistent device"),
            @ApiResponse(code = 400, message = "Invalid values")
    })
    @PutMapping
    public ResponseEntity<Preferences> updateAccount(@RequestBody Preferences model)
    {
        logger.info("PUT preferences", model);
        Preferences pref;
        try{
            pref = preferencesRepository.setPreferences(model);
            if(pref != null)
                return ResponseEntity.status(200).body(pref);

        }catch(PreferencesRepository.InvalidValuesException e)
        {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(404).body(null);
    }
}
