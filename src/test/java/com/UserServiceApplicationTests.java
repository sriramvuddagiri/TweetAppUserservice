package com;

import com.tweetapp.controller.UserServiceControllerTest;
import com.tweetapp.service.JwtUtilTest;
import com.tweetapp.service.ServiceTest;
import com.tweetapp.service.UserServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {UserServiceControllerTest.class, JwtUtilTest.class, ServiceTest.class, UserServiceTest.class})
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
