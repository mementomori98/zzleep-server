package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zzleep.core.models.TestModel;

@RestController
public class TestController {

    @GetMapping
    public ResponseEntity<TestModel> get() {
        return ResponseEntity
                .status(200)
                .body(new TestModel());
    }

}
