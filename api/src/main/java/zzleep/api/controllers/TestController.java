package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zzleep.api.services.TestService;
import zzleep.core.models.TestModel;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<TestModel> get() {
        return ResponseEntity
                .status(200)
                .body(testService.get());
    }

}
