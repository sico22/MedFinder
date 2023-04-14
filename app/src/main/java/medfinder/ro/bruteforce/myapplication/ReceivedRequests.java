package medfinder.ro.bruteforce.myapplication;

public class ReceivedRequests {

    String receivedUserDate, receivedUserName, receivedUserKey, receivedUserService;

    public ReceivedRequests(){

    }

    public ReceivedRequests(String receivedUserName, String receivedUserKey, String receivedUserDate, String receivedUserService){
        this.receivedUserDate = receivedUserDate;
        this.receivedUserName = receivedUserName;
        this.receivedUserKey = receivedUserKey;
        this.receivedUserService = receivedUserService;
    }

    public String getReceivedUserName(){
        return receivedUserName;
    }

    public String getReceivedUserDate(){
        return receivedUserDate;
    }

    public String getReceivedUserKey(){
        return receivedUserKey;
    }
    public String getReceivedUserService(){
        return receivedUserService;
    }

}
