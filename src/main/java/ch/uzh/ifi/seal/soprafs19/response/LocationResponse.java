package ch.uzh.ifi.seal.soprafs19.response;

public class LocationResponse {

    private String url;

    public LocationResponse(String url){
        this.url = url;
    }

    public LocationResponse(){}

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return this.url;
    }

}
