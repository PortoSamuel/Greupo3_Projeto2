package br.edu.insper.al.gustavobb.truckpad_insper;

import java.util.List;

public class GeoCodingLocation {
    private String country;
    private String state_acronym;
    private String state;
    private String postal_code;
    private String city;
    private String neighborhood;
    private String address;
    private String number;
    private String display_name;
    private List<Float> point;

    public String getCountry() {
        return country;
    }

    public String getState_acronym() {
        return state_acronym;
    }

    public String getState() {
        return state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public List<Float> getPoint() {
        return point;
    }
}
