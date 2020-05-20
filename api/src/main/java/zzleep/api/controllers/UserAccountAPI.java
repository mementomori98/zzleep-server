package zzleep.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zzleep.core.models.*;

@RestController
@RequestMapping("/user")
@Api(value = "User account api")
public class UserAccountAPI {

    @ApiOperation(value = "Create user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created an account"),
    })
    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody CreateUserModel model)
    {
        return ResponseEntity
                .status(200)
                .body(new User(1234, "email@gmail.com", "namee"));
    }

    @ApiOperation(value = "Update user account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated an account"),
    })
    @PutMapping
    public ResponseEntity updateAccount(@RequestBody UpdateUserModel model)
    {
        return ResponseEntity.status(200).body("");
    }

    @ApiOperation(value = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated a password"),
    })
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordModel model)
    {
        return ResponseEntity.status(200).body("");
    }

    @ApiOperation(value = "Get user account by Id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved account info"),
    })
    @GetMapping
    public ResponseEntity<User> updatePassword(@RequestParam(name = "userId") int userId)
    {
        return ResponseEntity
                .status(200)
                .body(new User(userId, "email@gmail.com", "namee"));
    }
}
