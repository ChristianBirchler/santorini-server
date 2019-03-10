package ch.uzh.ifi.seal.soprafs19.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
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
    @Before
    public void setUp() {
        super.setUp();

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");

        this.userService.createUser(user1);

    }


    @Test
    public void getUsersTest() throws Exception {
        String uri = "/users";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] users = super.mapFromJson(content, User[].class);
        assertTrue(users.length == 1);
    }



}
