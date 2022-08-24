package com.tweetapp.serviceimpl;


import com.tweetapp.exception.UnauthorizedException;
import com.tweetapp.exception.UserExistsException;
import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.LoginDetails;
import com.tweetapp.model.UserData;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.UserServices;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserServices{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  CustomerDetailsService custdetailservice;

    @Autowired
    private  JwtUtil jwtutil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public UserData getUserByUsername(String username)
    {
        return userRepository.findById(username).get();
    }

    @Override
    public String register(UserData userData){

        log.info("inside user service implementation to register user");
        Optional<UserData> user=userRepository.findById(userData.getUserName());
        if(!user.isPresent()) {
            try {
                
                if(userData.getPassword().equals(userData.getConfirmPassword())) {
                    String encodedpassword = passwordEncoder.encode(userData.getPassword());
                    userData.setPassword(encodedpassword);
                    userRepository.save(userData);
                    log.info("registered successfully");
                    return "User Added Successfully";
                    
                }
                else
                {
                   // return "Check the Confirm Password Should match with Password";
                    throw new UserExistsException("Check the Confirm Password Should match with Password");
                }
            }
            catch(RuntimeException ex) {
                
                if(!ex.getMessage().contains("Check the Confirm Password Should match with Password"))
                	{log.info("Some Unknown Error Occured ");
                  throw new UserExistsException("Some Unknown Error Occured ");}
                log.info("Check the Confirm Password Should match with Password");
                throw ex;
            }

        }
        else {
            log.info("Username already exists");
            throw new UserExistsException("Username already Exists");
        }
    }

    @Override
    public AuthResponse login(LoginDetails loginDetails){
        log.info("inside user service implementation to login");
        final UserDetails userdetails = custdetailservice.loadUserByUsername(loginDetails.getUsername());
        String uid = "";
        String generateToken = "";
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));
        Optional<UserData> user=userRepository.findById(userdetails.getUsername());
        uid = loginDetails.getUsername();
        generateToken = jwtutil.generateToken(userdetails);
        log.info("login successful");
        return new AuthResponse(uid,true,generateToken,user.get().getFirstName(),user.get().getLastName());//, HttpStatus.OK);

    }
    @Override
    public String forgotPassword(LoginDetails data){
        Optional<UserData> user=userRepository.findById(data.getUsername());
        if(!user.isPresent())
        {
        	log.info("user name not found");
         throw new UsernameNotFoundException("Username is not found please Check or Register");
        }
        String updateEncodedPassword=passwordEncoder.encode(data.getPassword());
        user.get().setPassword(updateEncodedPassword);
        userRepository.save(user.get());
        log.info("Password is reset successfully");
        return "Password is reset successfully,Kindly login again ";
    }

    @Override
    public List<UserData> searchByUsername(String token,String username){
        //List<UserData> usersList=userRepository.findAll().stream().filter(o->o.getUserName().contains(username)).collect(Collectors.toList());
        AuthResponse res=validate(token);
        if(res.isValid()) {
        	log.info("getting users with given username");
            return userRepository.findByUserNameContains(username);}
        throw new UnauthorizedException("Invalid Token");
        //return usersList;
    }

    @Override
    public List<UserData> getAllUsers(String token){
        AuthResponse res=validate(token);
        List<UserData> allUsersList = userRepository.findAll();
        if(res.isValid()) {
        	log.info("getting all the users");
            return allUsersList;
        }
        return allUsersList;
    }

    @Override
    public AuthResponse validate(String authToken) {
        String token1 = authToken.substring(7);
        AuthResponse res = new AuthResponse(null,false,null,null,null);
        if (Boolean.TRUE.equals(jwtutil.validateToken(token1))) {
            res.setUsername(jwtutil.extractUsername(token1));
            res.setValid(true);
            Optional<UserData> user1=userRepository.findById(jwtutil.extractUsername(token1));
            if(user1.isPresent()) {
                res.setUsername(user1.get().getUserName());
                res.setValid(true);
                res.setToken("token successfully validated");
                log.info("token successfully validated");
                return res;//new ResponseEntity<>(res, HttpStatus.OK);
            }
        } else {
            res.setValid(false);
            res.setToken("Invalid Token Received");
            log.info("At Validity : ");
            log.error("Token Has Expired");
            return res;//new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }
        return res;//new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
    }

}
