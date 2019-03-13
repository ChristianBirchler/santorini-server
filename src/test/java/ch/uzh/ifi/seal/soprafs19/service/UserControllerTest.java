package ch.uzh.ifi.seal.soprafs19.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import ch.uzh.ifi.seal.soprafs19.entity.Credentials;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.InvalidCredentialsException;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.response.UserResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;




public class UserControllerTest extends AbstractTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Override
    public void setUp() {
        super.setUp();

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");

        this.userService.createUser(user1);

    }


    @Test
    public void getUsersTest() throws Exception {

        userRepository.deleteAll();
        this.setUp();

        String uri = "/users";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] users = super.mapFromJson(content, User[].class);
        assertTrue(users.length >= 1);
    }


    @Test
    public void getUserByIdTest() throws Exception {

        userRepository.deleteAll();
        this.setUp();
        long id =this.userRepository.findByUsername("user1").getId();

        String strId = Long.toString(id);

        String uri = "/users/"+strId;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        UserResponse user = super.mapFromJson(content, UserResponse.class);

        Assert.assertEquals("user1", user.getUsername());
        Assert.assertNotNull(user.getCreationDate());
        Assert.assertNotNull(user.getId());


        // test case if user not found
        String strId2 = Long.toString(id+1);

        String uri2 = "/users/"+strId2;
        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.get(uri2)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status2 = mvcResult2.getResponse().getStatus();
        Assert.assertEquals("user id should be invalid!", 404, status2);


    }



    @Test
    public void checkCredentialsTest() throws Exception {
        Assert.fail();
    }

    @Test
    public void createUserTest() throws Exception {
        Assert.fail();
    }



    @Test
    public void updateUserTest() throws Exception {
        userRepository.deleteAll();
        setUp();
        User anotherUser = new User();
        anotherUser.setUsername("user2");
        anotherUser.setPassword("password2");
        userService.createUser(anotherUser);
        UserController userController = new UserController(userService);


        // new username (valid)
        User betterUser = new User();
        betterUser.setUsername("betterName");
        try {
            userController.updateUser(1, betterUser);
            Assert.assertEquals("Username are not equal!",
                    betterUser.getUsername(), userService.getUser(1).getUsername());
        }catch (UserAlreadyExistsException ex){
            Assert.fail("Username is already occupied!");
        }catch (UserNotFoundException ex){
            Assert.fail("User with corresponding id does not exist!");
        }

        // new username (invalid)
        try {
            userController.updateUser(1, betterUser);
            Assert.fail("username \"betterName\" should already be occupied!");
        }catch (UserAlreadyExistsException ex){
            Assert.assertTrue(true);
        }catch (UserNotFoundException ex){
            Assert.fail("User with corresponding id does not exist!");
        }

        try {
            userController.updateUser(2, betterUser);
            Assert.fail("username \"betterName\" should already be occupied!");
        }catch (UserAlreadyExistsException ex){
            Assert.assertTrue(true);
            Assert.assertEquals("username must not be changed!",
                    anotherUser.getUsername(), userService.getUser(2).getUsername());
        }catch (UserNotFoundException ex){
            Assert.fail("User with corresponding id does not exist!");
        }





        // new password
        User betterUser2 = new User();
        betterUser2.setPassword("geheim");
        try {
            userController.updateUser(1, betterUser2);
            Assert.assertEquals("password should be updated!",
                    betterUser2.getPassword(), userService.getUser(1).getPassword());
        } catch (RuntimeException ex){
            Assert.fail("Did not update password; reason: "+ex.getMessage());
        }




    }



}
