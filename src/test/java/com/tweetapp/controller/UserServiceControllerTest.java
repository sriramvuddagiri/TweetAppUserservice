package com.tweetapp.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.LoginDetails;
import com.tweetapp.model.UserData;
import com.tweetapp.service.UserServices;


@ExtendWith(MockitoExtension.class)
public class UserServiceControllerTest {
    @Mock
    UserServices userServices;

    @InjectMocks
    UserServiceController userController;

    UserDetails userdetails;
    UserData userData;
    Optional<UserData> user;
    LoginDetails data;

    @Test
    void register() {

        UserData userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        ResponseEntity<Object> newObject=new ResponseEntity<>("User Added Successfully", HttpStatus.CREATED);
        when(userServices.register(userData)).thenReturn(newObject.toString());
        assertEquals(201, userController.register(userData).getStatusCodeValue());
    }
    @Test
    void login() {
        data=new LoginDetails("Skrishna14","admin12345");
       AuthResponse authResponse=new AuthResponse("Skrishna14",true,"Bearer token","Krishna","Naga");
       when(userServices.login(data)).thenReturn(authResponse);
        assertEquals(200, userController.login(data).getStatusCodeValue());
    }

    @Test
    void forgotPassword() {
        data=new LoginDetails("Skrishna14","admin12345");
        when(userServices.forgotPassword(data)).thenReturn(new ResponseEntity<>(new AuthResponse(),HttpStatus.OK).toString());
        assertEquals(200, userController.forgotPassword("kumar", data).getStatusCodeValue());
    }
    @Test
    void validate() {
        when(userServices.validate("Bearer token")).thenReturn(new AuthResponse());
        assertEquals(204, userController.getValidity("Bearer token").getStatusCodeValue());
    }
    @Test
    void getAllUsers() {

        when(userServices.getAllUsers("Bearer token")).thenReturn(new ArrayList<>());
        assertEquals(200, userController.getALLUsers("Bearer token").getStatusCodeValue());
    }
    @Test
    void searchuser() {

        when(userServices.searchByUsername("Bearer token","sri")).thenReturn(new ArrayList<>());
        assertEquals(200, userController.searchByUsername("Bearer token","sri").getStatusCodeValue());
    }

}
