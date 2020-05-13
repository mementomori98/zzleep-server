package zzleep.api.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.logging.Logger;
import zzleep.core.repositories.TestRepository;
import zzleep.core.models.TestModel;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "Test api")
public class TestController {

    private final TestRepository testRepository;
    private final Logger logger;

    public TestController(
        TestRepository testRepository, Logger logger
    ) {
        this.testRepository = testRepository;
        this.logger = logger;
    }

    @ApiOperation(value = "Get a TestModel object", response = TestModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved TestModel"),
            @ApiResponse(code = 500, message = "A server error occured"),
    })
    @GetMapping
    public ResponseEntity<List<TestModel>> getAll()
    {
        return ResponseEntity
                .status(200)
                .body(testRepository.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestModel> get(@PathVariable int id) {
        return ResponseEntity
            .status(200)
            .body(testRepository.get(id));
    }

    @PutMapping
    public ResponseEntity<TestModel> update(@RequestBody TestModel model) {
        return ResponseEntity
            .status(200)
            .body(testRepository.update(model));
    }

    @PostMapping
    public ResponseEntity<TestModel> create(@RequestBody TestModel model) {
        return ResponseEntity
            .status(200)
            .body(testRepository.add(model));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam int id) {
        testRepository.delete(id);
        return ResponseEntity
            .status(200)
            .build();
    }

}
