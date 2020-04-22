package zzleep.api.controllers;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zzleep.core.services.TestService;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/api")
@Api(value = "Test api")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @ApiOperation(value = "Get a TestModel object", response = TestModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved TestModel"),
            @ApiResponse(code = 500, message = "A server error occured"),
    })
    @GetMapping(produces = { "application/json " })
    public ResponseEntity<TestModel> get(@ApiParam(value = "Some value", required = true) @RequestParam String message)
    {
        return ResponseEntity
                .status(200)
                .body(new TestModel(message));
    }

}
