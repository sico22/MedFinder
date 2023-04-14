package medfinder.ro.bruteforce.myapplication;

import java.util.ArrayList;

public class AvailableServicesList {
    ArrayList<String>availableServices;

    public AvailableServicesList(ArrayList<String>availableServices){
        this.availableServices = availableServices;
    }

    public ArrayList<String> getAvailableServices() {
        return availableServices;
    }
}
