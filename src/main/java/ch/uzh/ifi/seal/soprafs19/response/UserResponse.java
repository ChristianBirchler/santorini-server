package ch.uzh.ifi.seal.soprafs19.response;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;

import java.util.Date;

public class UserResponse {

    private long id;
    private String username;
    private Date creationDate;
    private boolean loggedIn;
    private Date birthday;

    public UserResponse(User user){

        this.id = user.getId();
        this.username = user.getUsername();

        // TODO creation date as field in User entity

        this.loggedIn = (UserStatus.ONLINE == user.getStatus());

        // TODO birthday as field in User entity

    }


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
