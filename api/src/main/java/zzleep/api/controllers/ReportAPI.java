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
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/report")
@Api(value = "Report api")
public class ReportAPI {

    @ApiOperation(value = "Get a report on sleep")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a report"),
    })
    @GetMapping
    public ResponseEntity<String> getReport(@RequestParam(name = "devId") String deviceId)
    {
        return ResponseEntity
                .status(200)
                .body("A Report for device with id #" + deviceId);
    }
}
