package com.tweetapp.controller;


import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.LoginDetails;
import com.tweetapp.model.UserData;
import com.tweetapp.service.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/apps/v1.0/tweets")
public class UserServiceController {

    @Autowired
    private UserServices userService;

    @PostMapping("/register")
    @Operation(summary = "Registering for new User",description = "A Post request for Registering for new User",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully registered the new user"),
            @ApiResponse(responseCode = "400",description = "Input Validation Failed"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    public ResponseEntity<Object>  register(@Valid @RequestBody UserData user){

        log.info("In user service-Registering User");
		log.debug("registering user {}",user);
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);


    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserData> getUserByUsername(@PathVariable String username)
    {
        return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
    }

    @Operation(summary = "Log in for Existing User",description = "A Post request for Log in for Existing User",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully logged in the user"),
            @ApiResponse(responseCode = "403",description = "Un-Successfully logged in the user"),
            @ApiResponse(responseCode = "400",description = "Input Validation Failed"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody  LoginDetails userlogincreds){

        AuthResponse authResponse=userService.login(userlogincreds);
        
        if(authResponse.isValid())
        {
        	log.info("inside user service to login");
    		log.debug("Login user name: {}",userlogincreds.getUsername());
            return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>(authResponse,HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Forgot password",description = "A Put request for Forgot Password",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Password is reset successfully"),
            @ApiResponse(responseCode = "400",description = "Input Validation Failed"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    @PutMapping(value="/{username}/forgot")
    public ResponseEntity<Object> forgotPassword(@PathVariable String username,  @RequestBody LoginDetails data)
    {
    	log.info("inside user service to change forgot password {}",data.getUsername());
        data.setUsername(username);
        return new ResponseEntity<>(userService.forgotPassword(data), HttpStatus.OK);

    }

    @Operation(summary = "Search User by username",description = "A get request for getting user by username",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully got the user"),
            @ApiResponse(responseCode = "204",description = "No users Found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    @GetMapping(value = "/users/search/{username}")
    public ResponseEntity<Object> searchByUsername(@RequestHeader("Authorization") String token,@PathVariable String username){
        log.info("inside user service to search user by given username");
        List<UserData> userData=userService.searchByUsername(token,username);
        if(!userData.isEmpty())
            return new ResponseEntity<>(userData,HttpStatus.OK);
        return new ResponseEntity<>("No User Found based on Request Try Again!!!",HttpStatus.OK);
    }

    @Operation(summary = "Getting all Users",description = "A get request for getting all Users",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Successfully got all the users"),
            @ApiResponse(responseCode = "204",description = "No Users Found"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    @GetMapping(value="/users/all")
    public ResponseEntity<Object> getALLUsers(@RequestHeader("Authorization") String token){
        List<UserData> userData=userService.getAllUsers(token);
        log.info("inside user service to get all users");
        if(!userData.isEmpty())
            return new ResponseEntity<>(userData,HttpStatus.OK);
        return new ResponseEntity<>("No Users Found on DataBase",HttpStatus.OK);

    }

    @Operation(summary = "Validate the token",description = "A Get request for Validate the User token",tags = {"User Service API"})
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200",description = "Validated the user token"),
            @ApiResponse(responseCode = "500",description = "Some Exception Occured")
    })
    @GetMapping(value = "/validate")
    public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization")final String token) {
        log.info("inside user service to validate the token");
        AuthResponse authResponse=userService.validate(token);
        if(authResponse.isValid())
        {
            return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>(authResponse,HttpStatus.NO_CONTENT);
    }


}
