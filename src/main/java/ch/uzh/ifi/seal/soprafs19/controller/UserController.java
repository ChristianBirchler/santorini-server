package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.response.LocationResponse;
import ch.uzh.ifi.seal.soprafs19.response.UserResponse;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }



    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    LocationResponse createUser(@RequestBody User newUser){

        this.service.createUser(newUser);

        long id = newUser.getId();

        String strId = Long.toString(id);

        String url = "/users/"+strId;
        return new LocationResponse(url);
    }


    @GetMapping("/users/{id}")
    UserResponse getUser(@PathVariable long id){

        User user = this.service.getUser(id);

        return new UserResponse(user);
    }



    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@PathVariable long id, @RequestBody User user){
        this.service.updateUser(id, user);
    }



}
