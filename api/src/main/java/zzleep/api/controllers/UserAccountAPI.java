package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.TestModel;

@RestController
@RequestMapping("/user")
@Api(value = "User account api")
public class UserAccountAPI {

    @ApiOperation(value = "Create user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created an account"),
    })
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Account created?");
    }

    @ApiOperation(value = "Update user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated an account"),
    })
    @PutMapping
    public ResponseEntity<String> updateAccount(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Account updated?");
    }

    @ApiOperation(value = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a password"),
    })
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody TestModel model)
    {
        return ResponseEntity
                .status(200)
                .body("Account password updated?");
    }

    @ApiOperation(value = "Get user account by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved account info"),
    })
    @GetMapping
    public ResponseEntity<String> updatePassword(@RequestParam(name = "userId") String userId)
    {
        return ResponseEntity
                .status(200)
                .body("Retrieved info for acount " + userId);
    }
}
