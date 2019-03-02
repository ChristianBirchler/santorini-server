package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {

        Assert.assertNull("initially repository must be empty", userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("secret");
        userService.createUser(testUser);


        Assert.assertNotNull("no token", testUser.getToken());
        Assert.assertEquals("user status is not online", testUser.getStatus(),UserStatus.ONLINE);
        Assert.assertEquals("find by token does not work", testUser, userRepository.findByToken(testUser.getToken()));


    }


    @Test
    public void getUser(){

        User user2 = new User();
        user2.setUsername("lisa");
        user2.setPassword("i_love_milhouse");
        userService.createUser(user2);

        User user3 = new User();
        user3.setUsername("maggie");
        user3.setPassword("hihi");
        userService.createUser(user3);


        User returnedUser = userService.getUser(user2.getId());
        Assert.assertEquals(returnedUser, user2);

        returnedUser = userService.getUser(user3.getId());
        Assert.assertEquals(returnedUser, user3);

        Assert.assertNotEquals(returnedUser, user2);


    }


    @Test
    public void updateUser() {

        // TODO birthday: which format (Date object or only string???)

        User user4 = new User();
        user4.setUsername("nelson");
        user4.setPassword("hahahahahaha!!!");
        userService.createUser(user4);

        user4.setUsername("flanders");
        user4.setPassword("jesus");


        User retUser = userService.getUser(1);

        Assert.assertNotEquals(user4.getUsername(), retUser.getUsername());
        Assert.assertNotEquals(user4.getPassword(), retUser.getPassword());


        userService.updateUser(1, user4);
        retUser = userService.getUser(1);
        Assert.assertEquals(user4.getUsername(), retUser.getUsername());
        Assert.assertEquals(user4.getPassword(), retUser.getPassword());
        Assert.assertEquals(user4, retUser);


    }

}
