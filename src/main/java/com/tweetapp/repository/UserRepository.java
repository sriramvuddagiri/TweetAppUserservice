package com.tweetapp.repository;


import com.tweetapp.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserData,String> {

    public List<UserData> findByUserNameContains(String userName);
}
