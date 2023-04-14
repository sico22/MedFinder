package medfinder.ro.bruteforce.myapplication;

public class UserInformation {
    String name, phone, address, uid;
    Boolean switchul;
    int identity;
    float ratingCentru;


    public UserInformation(){

    }


    public UserInformation(String name, String phone, String address, int identity, boolean switchul, float ratingCentru) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.identity = identity;
        this.switchul = switchul;
        this.ratingCentru = ratingCentru;
    }

    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public float getRatingCentru(){return ratingCentru;}
    public int getIdentity(){
        return identity;
    }
    public Boolean getSwitchul() {
        return switchul;
    }
    private void setSwitchul(Boolean switchul){this.switchul = switchul;}
    private void setRatingCentru(float ratingCentru){this.ratingCentru = ratingCentru;}
    private void setName(String name){
        this.name = name;
    }
    private void setAddress(String address){
        this.address = address;
    }
    private void setPhone(String phone){
        this.phone = phone;
    }
    private void setIdentity(int identity){
        this.identity = identity;
    }
}
