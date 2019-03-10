package ch.uzh.ifi.seal.soprafs19.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.uzh.ifi.seal.soprafs19.entity.User;
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


        // TODO test case if user not found



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
        Assert.fail();
    }



}
