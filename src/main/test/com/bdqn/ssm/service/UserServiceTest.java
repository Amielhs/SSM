package com.bdqn.ssm.service;

import com.bdqn.ssm.pojo.User;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationContext.xml")
public class UserServiceTest {

    Logger logger = Logger.getLogger(this.getClass());
    ApplicationContext context = null;
    @Before
    public void setUp() throws Exception {
        context =new ClassPathXmlApplicationContext("spring/applicationContext.xml");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUserList() {
        UserService userService= (UserService) context.getBean("userService");
        List<User> userList = null;
        userList= userService.getUserList(null,0,0,5);
        for (User user:
                userList) {
            logger.info(user.getId()+"——"+user.getUserName()+"——"+user.getRole().getRoleName());
        }
    }
}