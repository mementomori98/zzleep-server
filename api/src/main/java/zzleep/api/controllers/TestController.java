package zzleep.api.controllers;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzleep.core.logging.Logger;
import zzleep.core.services.TestService;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/api")
@Api(value = "Test api")
public class TestController {

    private final TestService testService;
    private final Logger logger;

    public TestController(
            TestService testService,
            Logger logger
    ) {
        this.testService = testService;
        this.logger = logger;
    }

    @ApiOperation(value = "Get a TestModel object", response = TestModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved TestModel"),
            @ApiResponse(code = 500, message = "A server error occured"),
    })
    @GetMapping
    public ResponseEntity<TestModel> get()
    {
        return ResponseEntity
                .status(200)
                .body(testService.get());
    }

}
