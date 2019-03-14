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
        /*
        This method checks if the credentials of the login are correct. If so it will return a LoginResponse object.
         */

        String username = cred.getUsername();
        String password = cred.getPassword();

        // check if username exists
        if (!this.userRepository.existsByUsername(username)){
            throw new InvalidCredentialsException();
        }

        // take the user from repo and save it into a temporary variable 'user'
        User user = this.userRepository.findByUsername(username);

        // check if the password is valid
        if (!user.getPassword().equals(password)){
            throw new InvalidCredentialsException();
        }

        // if credentials are correct, return a LoginResponse with the token in it
        String token = this.userRepository.findByUsername(username).getToken();

        return new LoginResponse(token);

    }



    public void createUser(User newUser) {

        // Check if a username with the corresponding username already exist. If so then throw exception.
        if(this.userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UserAlreadyExistsException();
        }

        // create token and save the user with its token into the repository
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(new Date());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);


    }





    public User getUser(long id){

        // If no user exists with the given id, then throw a exception.
        if(!this.userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        return this.userRepository.findById(id);
    }




    public void updateUser(long id, User user){

        // check if user with the given id exist. otherwise throw an exception.
        if(!this.userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        // get the user who should be updated
        User updateUser = this.userRepository.findById(id);

        // if user name is new and valid, then set the new username.
        if(user.getUsername() != null){
            if(this.userRepository.findByUsername(user.getUsername()) != null){ // username is already occupied
                throw new UserAlreadyExistsException();
            }
            updateUser.setUsername(user.getUsername());
        }
        if(user.getPassword() != null){ // if password should be updated
            updateUser.setPassword(user.getPassword());
        }
        if(user.getBirthday() != null){ // if birthday date should be updated
            updateUser.setBirthday(user.getBirthday());
        }

        this.userRepository.save(updateUser);

    }


}
