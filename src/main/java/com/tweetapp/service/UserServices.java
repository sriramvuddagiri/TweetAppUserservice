package com.tweetapp.service;

import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.LoginDetails;
import com.tweetapp.model.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServices {

    public AuthResponse login(LoginDetails loginDetails);
    public String register(UserData user);
   public String forgotPassword(LoginDetails data);
    public AuthResponse validate(String authToken);
    public List<UserData> searchByUsername(String token,String username);
    public List<UserData> getAllUsers(String token);
    public UserData getUserByUsername(String username);


}
