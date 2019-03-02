package ch.uzh.ifi.seal.soprafs19.response;

public class ErrorResponse {

    private String reason;

    public ErrorResponse(String reason){
        this.reason = reason;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
