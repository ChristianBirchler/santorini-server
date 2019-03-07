package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Credentials;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.InvalidCredentialsException;
import ch.uzh.ifi.seal.soprafs19.exception.UserAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.exception.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.response.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }


    public LoginResponse checkCredentials(Credentials cred){
        String username = cred.getUsername();
        String password = cred.getPassword();

        if (!this.userRepository.existsByUsername(username)){
            throw new InvalidCredentialsException();
        }

        User user = this.userRepository.findByUsername(username);

        if (!user.getPassword().equals(password)){
            throw new InvalidCredentialsException();
        }


        String token = this.userRepository.findByUsername(username).getToken();

        return new LoginResponse(token);

    }



    public void createUser(User newUser) {

        if(this.userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UserAlreadyExistsException();
        }

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(new Date());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);


    }





    public User getUser(long id){


        if(!this.userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        return this.userRepository.findById(id);
    }




    public void updateUser(long id, User user){


        if(!this.userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }


        User updateUser = this.userRepository.findById(id);


        if(user.getUsername() != null){
            if(this.userRepository.findByUsername(user.getUsername()) != null){ // username is already occupied
                throw new UserAlreadyExistsException();
            }
            updateUser.setUsername(user.getUsername());
        }
        if(user.getPassword() != null){
            updateUser.setPassword(user.getPassword());
        }
        if(user.getBirthday() != null){
            updateUser.setBirthday(user.getBirthday());
        }

        this.userRepository.save(updateUser);

    }


}
