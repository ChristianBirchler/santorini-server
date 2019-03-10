package ch.uzh.ifi.seal.soprafs19.response;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;

import java.util.Date;

public class UserResponse {

    /*
    This class is used for the response on the endpoint GET /users/{id}.
    Take a look on the specification from Assignment1.pdf!
     */


    private long id;
    private String username;
    private Date creationDate;
    private boolean loggedIn;
    private Date birthday;

    public UserResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.creationDate = user.getCreationDate();
        this.loggedIn = (UserStatus.ONLINE == user.getStatus());
        this.birthday = user.getBirthday();
    }

    public UserResponse(){}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }



}
