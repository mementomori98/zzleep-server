package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import zzleep.core.models.Fact;

// @RestController
// @RequestMapping("/facts")
// @Api(value = "Fact api")
public class FactsController {

    // @ApiOperation(value = "Get a fact about sleep", response = Fact.class)
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Successfully retrieved a fact"),
    // })
    @GetMapping
    public ResponseEntity<Fact> getFact()
    {
        return ResponseEntity
                .status(200)
                .body(new Fact("Sleep fact 01", "Don't eat before you sleep"));
    }
}
