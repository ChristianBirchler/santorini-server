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
import ch.uzh.ifi.seal.soprafs19.response.LocationResponse;
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

        this.setUp();
        String uri = "/users/credentials";


        // valid credentials
        Credentials credentials = new Credentials();
        credentials.setUsername("user1");
        credentials.setPassword("password1");
        String inputJson = super.mapToJson(credentials);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("status code is not 200!", 200, status);
        String content = mvcResult.getResponse().getContentAsString();
        LoginResponse loginResponse = super.mapFromJson(content, LoginResponse.class);
        Assert.assertNotNull("There must be a token!", loginResponse.getToken());


        // correct username, wrong password
        credentials.setUsername("user1");
        credentials.setPassword("wrongPassword");
        inputJson = super.mapToJson(credentials);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("404 response status should be set since password is wrong!", 404, status);


        // wrong username, correct password
        credentials.setUsername("wrongUsername");
        credentials.setPassword("password1");
        inputJson = super.mapToJson(credentials);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("404 response status should be set since username is wrong!", 404, status);



        // wrong username, wrong password
        credentials.setUsername("wrongUsername");
        credentials.setPassword("wrongPassword");
        inputJson = super.mapToJson(credentials);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("404 response status should be set since username and password are wrong!", 404, status);


    }

    @Test
    public void createUserTest() throws Exception {
        this.userRepository.deleteAll();
        this.setUp();
        String uri = "/users";
        User user = new User();

        // normal case
        user.setUsername("newUser");
        user.setPassword("myPassword");
        String inputJson = super.mapToJson(user);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("status code is not 201!", 201, status);
        Assert.assertEquals("username is not properly saved in repository!",
                user.getUsername(), userRepository.findByUsername(user.getUsername()).getUsername());
        Assert.assertEquals("password is not properly saved in repository!",
                user.getPassword(), userRepository.findByUsername(user.getUsername()).getPassword());
        LocationResponse locationResponse = super.mapFromJson(mvcResult.getResponse().getContentAsString(), LocationResponse.class);
        String strId = Long.toString(userRepository.findByUsername(user.getUsername()).getId());
        Assert.assertEquals("Wrong url in response body!", "/users/"+strId, locationResponse.getUrl());


        // username is already occupied
        user.setUsername("user1");
        user.setPassword("anotherPassword");
        inputJson = super.mapToJson(user);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("User should already exist!", 409, status);


    }



    @Test
    public void updateUserTest() throws Exception {
        userRepository.deleteAll();
        setUp();
        User anotherUser = new User();
        anotherUser.setUsername("user2");
        anotherUser.setPassword("password2");
        userService.createUser(anotherUser);
        User betterUser = new User();
        String uri = "/users/";
        long id = userRepository.findByUsername("user1").getId();
        String strId = Long.toString(id);

        // new username (valid)
        betterUser.setUsername("betterUsername");
        String inputJson = super.mapToJson(betterUser);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri+strId)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("There must not be any response body!", 204, status);
        Assert.assertEquals("username is not updated!",
                betterUser.getUsername(), userRepository.findById(id).getUsername());


        // new username (invalid)
        betterUser.setUsername("user2");
        inputJson = super.mapToJson(betterUser);
        strId = Long.toString(userRepository.findByUsername("user2").getId());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri+strId)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals("username should already exist!", 409, status);



        // new password
        User betterUser2 = new User();
        betterUser2.setPassword("newPassword");
        inputJson = super.mapToJson(betterUser2);
        id = userRepository.findByUsername("user2").getId();
        strId = Long.toString(id);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri+strId)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        Assert.assertEquals("HTTP status is not correct!", 204, mvcResult.getResponse().getStatus());
        Assert.assertEquals("Password has not changed!",
                betterUser2.getPassword(), userRepository.findById(id).getPassword());

        // new username and new password
        betterUser.setUsername("reallyNewUsername");
        betterUser.setPassword("reallySecret");
        inputJson = super.mapToJson(betterUser);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri+strId)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        Assert.assertEquals("HTTP status is not properly set!", 204, mvcResult.getResponse().getStatus());
        Assert.assertEquals("username has not changed!",
                betterUser.getUsername(), userRepository.findById(id).getUsername());
        Assert.assertEquals("password has not changed!",
                betterUser.getPassword(), userRepository.findById(id).getPassword());


        // user id not found
        betterUser.setUsername("newName");
        betterUser.setPassword("newPassword");
        inputJson = super.mapToJson(betterUser);

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri+"398")
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        Assert.assertEquals("id (398) should not exist",404, mvcResult.getResponse().getStatus());




    }



}
