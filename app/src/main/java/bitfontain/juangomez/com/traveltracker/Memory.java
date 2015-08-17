package bitfontain.juangomez.com.traveltracker;

import java.io.Serializable;

/**
 * Created by Juan on 8/11/2015.
 */
//This class is in charge of modeling the data and all the logic arround that
public class Memory implements Serializable {

    long id;
    double latitude;
    double longitude;
    String city;
    String country;
    String notes;
}
