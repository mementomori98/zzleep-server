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
import zzleep.core.models.Fact;

import zzleep.core.services.Authorized;
import zzleep.core.services.FactService;

import java.util.List;

@RestController
@RequestMapping("/api/facts")
@Api(tags = {"Facts"}, description = " ")
public class FactsController extends ControllerBase {

    private final FactService factService;


    public FactsController(FactService factService) {
        this.factService = factService;

    }

    @ApiOperation(value = "Get a random fact", response = Fact.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a fact")
    })
    @GetMapping("/random")
    public ResponseEntity<Fact> getFact(@RequestParam(name = "previousFactId", defaultValue = "-1") int factId)
    {
        return map(factService.getFact(new Authorized<>(userId(),factId)));
    }

    @ApiOperation(value = "Get all facts", response = Fact[].class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all facts")
    })
    @GetMapping
    public ResponseEntity<List<Fact>> getAll() {
        return map(factService.getAll(new Authorized<>(userId())));
    }

}
