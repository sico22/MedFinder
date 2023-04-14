package medfinder.ro.bruteforce.myapplication;

public class Appointments {

    String userDate, userService, userHour, userName;
    public Appointments(){

    }
    public Appointments(String userName, String userService, String userDate, String userHour){
        this.userDate = userDate;
        this.userHour = userHour;
        this.userService = userService;
        this.userName = userName;
    }

    public String getUserDate() {
        return userDate;
    }

    public String getUserHour() {
        return userHour;
    }

    public String getUserService() {
        return userService;
    }

    public String getUserName() {
        return userName;
    }
}
