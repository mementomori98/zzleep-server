package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.logging.Logger;
import zzleep.core.models.Preferences;
import zzleep.core.repositories.AuthorizationService;
import zzleep.core.repositories.PreferencesRepository;
import zzleep.core.services.Authorized;
import zzleep.core.services.PreferencesService;

@RestController
@RequestMapping("/api/preferences")
@Api(tags = {"Preferences"}, description = " ")
public class PreferencesController extends ControllerBase {

    private final PreferencesService preferencesService;

    public PreferencesController(PreferencesService preferencesService){
        this.preferencesService = preferencesService;
    }

    @ApiOperation(value = "Get current preferences", response = Preferences.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved preferences"),
        @ApiResponse(code = 403, message = "This user does not own a device with this ID")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<Preferences> getPreferences(
        @PathVariable(name = "deviceId") String deviceId
    ) {
        return map(preferencesService.getByDeviceId( new Authorized<>(userId(), deviceId)));
    }

    @ApiOperation(value = "Update user preferences")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated preferences"),
        @ApiResponse(code = 400, message = "Invalid values (a min value is greater than a max value)"),
        @ApiResponse(code = 403, message = "The user does not own a device with this ID")
    })
    @PutMapping
    public ResponseEntity<Preferences> updatePreferences(
        @RequestBody Preferences model
    ) {
        return map(preferencesService.update(new Authorized<>(userId(), model)));
    }
}
