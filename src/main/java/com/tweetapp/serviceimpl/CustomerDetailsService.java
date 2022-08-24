package com.tweetapp.serviceimpl;

import com.tweetapp.exception.UnauthorizedException;
import com.tweetapp.model.UserData;
import com.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class CustomerDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userdao;


    @Override
    public UserDetails loadUserByUsername(String uname) {

        try {
            log.info("loading the user");
            Optional<UserData> user = userdao.findById(uname);
           // if (user.isPresent()) {
                return new User(user.get().getUserName(), user.get().getPassword(), new ArrayList<>());
            //}

        } catch (Exception e) {
            log.info("exception occured");
            throw new UnauthorizedException("Username Not Found Exception");
        }


    }

}
