package ch.uzh.ifi.seal.soprafs19.response;

public class LoginResponse {

    private String token;


    public LoginResponse(String token){
        this.token = token;
    }

    public LoginResponse(){}


    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
