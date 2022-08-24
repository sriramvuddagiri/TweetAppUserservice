package com.tweetapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.tweetapp.serviceimpl.CustomerDetailsService;
import com.tweetapp.serviceimpl.JwtUtil;
import com.tweetapp.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tweetapp.exception.UserExistsException;
import com.tweetapp.model.LoginDetails;

import com.tweetapp.model.UserData;
import com.tweetapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerDetailsService custdetailservice;
    @Mock
    private JwtUtil jwtutil;
    @InjectMocks
    private UserServiceImpl userService;


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    UserDetails userdetails;
    UserData userData;
    Optional<UserData> user;

   @Test
    void loginTest() {
        userdetails=new User("Skrishna13", "admin12345", new ArrayList<>());
        when(custdetailservice.loadUserByUsername("Skrishna13")).thenReturn(userdetails);
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna13","admin12345","admin12345",999999990);
        when(userRepository.findById("Skrishna13")).thenReturn(Optional.of(userData));
        LoginDetails login=new LoginDetails("Skrishna13","admin12345");
       when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()))).thenReturn(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()));
       when(jwtutil.generateToken(userdetails)).thenReturn("token");
       assertEquals(true,userService.login(login).isValid());
    }

    @Test
    void loginTestFail() {
        userdetails=new User("Skrishna14", "admin12345", new ArrayList<>());
        when(custdetailservice.loadUserByUsername("Skrishna14")).thenReturn(userdetails);
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.of(userData));
        LoginDetails login=new LoginDetails("Skrishna14","admin1234");
        assertEquals(null, userService.login(login).getToken());
    }

    @Test
    void registerTest() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna15","admin12345","admin12345",999999990);
        when(userRepository.findById("Skrishna15")).thenReturn(Optional.ofNullable(null));
        when(userRepository.save(userData)).thenReturn(userData);
        assertEquals("User Added Successfully",userService.register(userData));
    }

    @Test
    void registerTestFail() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna13","admin12345","admin123456",999999990);
        when(userRepository.findById("Skrishna13")).thenReturn(Optional.ofNullable(userData));
        assertThrows(UserExistsException.class,()->userService.register(userData));
    }
    @Test
    void registerTestFail2() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        lenient().when(userRepository.findById("Skrishna14")).thenReturn(Optional.of(userData));
        lenient().when(userRepository.save(userData)).thenThrow(RuntimeException.class);
        assertThrows(UserExistsException.class,()-> userService.register(userData));
    }

    @Test
    void getAllUsers() {
        List<UserData> users=new ArrayList<>();
        users.add(new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990));
        users.add(new UserData("Krishna","Naga","Naga1@gmail.com","Skrishna15","admin12345","admin12345",999999990));
        userdetails = new User("Skrishna14", "admin12345", new ArrayList<>());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users, userService.getAllUsers("Bearer token"));
    }

    @Test
    void getUsersByUsername() {
        List<UserData> users=new ArrayList<>();
        users.add(new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990));
        users.add(new UserData("Krishna","Naga","Naga1@gmail.com","Skrishna15","admin12345","admin12345",999999990));
        when(userRepository.findByUserNameContains("r")).thenReturn(users);
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        when(jwtutil.validateToken("token")).thenReturn(true);
        when(jwtutil.extractUsername("token")).thenReturn("Skrishna14");
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.ofNullable(userData));
        assertEquals(users, ( userService.searchByUsername("Bearer token","r")));
    }

    @Test
    void forgotPassword() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        LoginDetails login=new LoginDetails("Skrishna14","admin123456");
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.ofNullable(userData));
        when(userRepository.save(userData)).thenReturn(userData);
        assertEquals("Password is reset successfully,Kindly login again ", userService.forgotPassword(login));
    }

    @Test
    void forgotPasswordFail() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        LoginDetails login=new LoginDetails("Skrishna14","kuma");
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.ofNullable(null));
        assertThrows(UsernameNotFoundException.class,()-> userService.forgotPassword(login));
    }
    @Test
    void validateToken() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        when(jwtutil.validateToken("token")).thenReturn(true);
        when(jwtutil.extractUsername("token")).thenReturn("Skrishna14");
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.ofNullable(userData));
        assertEquals(true, userService.validate("Bearer token").isValid());
    }
    @Test
    void validateTokenFail() {
        userData=new UserData("Krishna","Naga","Naga@gmail.com","Skrishna14","admin12345","admin12345",999999990);
        when(jwtutil.validateToken("token")).thenReturn(true);
        when(jwtutil.extractUsername("token")).thenReturn("Skrishna14");
        when(userRepository.findById("Skrishna14")).thenReturn(Optional.ofNullable(null));
        assertEquals(null, userService.validate("Bearer token").getToken());
    }
    @Test
    void validateTokenFail2() {
        when(jwtutil.validateToken("token")).thenReturn(false);
        assertEquals("Invalid Token Received", userService.validate("Bearer token").getToken());
    }
}
