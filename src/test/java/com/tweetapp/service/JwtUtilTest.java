package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import com.tweetapp.serviceimpl.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;




@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {
    @Mock
    UserDetails userdetails;



    @Test
    void generateTokenTest() {
        userdetails = new User("Skrishna13", "admin12345", new ArrayList<>());
        JwtUtil jwt = new JwtUtil();
        String generateToken = jwt.generateToken(userdetails);
        assertNotNull(generateToken);
    }


    @Test
    void validateTokenTest() {
        userdetails = new User("Skrishna13", "admin12345", new ArrayList<>());
        JwtUtil jwt = new JwtUtil();
        String generateToken = jwt.generateToken(userdetails);
        Boolean validateToken = jwt.validateToken(generateToken);
        assertEquals(true, validateToken);
    }

    @Test
    void validateTokenNegativeTest() {
        userdetails = new User("Skrishna13", "admin12345", new ArrayList<>());
        JwtUtil jwt = new JwtUtil();
       // String generateToken = jwt.generateToken(userdetails);
        Boolean validateToken = jwt.validateToken(null);
        assertEquals(false, validateToken);
    }


}
