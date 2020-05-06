package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/facts")
@Api(value = "Fact api")
public class FactAPI {

    @ApiOperation(value = "Get a fact about sleep")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a fact"),
    })
    @GetMapping
    public ResponseEntity<String> getFact()
    {
        return ResponseEntity
                .status(200)
                .body("A Random Fact");
    }
}
