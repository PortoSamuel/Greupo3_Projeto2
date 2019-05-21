package br.edu.insper.al.gustavobb.truckpad_insper;

import java.util.LinkedList;
import java.util.List;

public class GeoPlace {
    private String country = "Brasil";
    private String state_acronym = "SP";
    private String state = "São Paulo";
    private String postal_code = "04546-000";
    private String city = "São Paulo";
    private String neighborhood = "Itaim Bibi";
    private String address = "Rua Fidêncio Ramos";
    private String number = "302";
    private String display_name = "Rua Fidêncio Ramos 302, Itaim Bibi, São Paulo - SP";
    private List<Double> point = new LinkedList<>();

    public void putPoint(double number){
        this.point.add(number);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState_acronym(String state_acronym) {
        this.state_acronym = state_acronym;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setPoint(List<Double> point) {
        this.point = point;
    }

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

    public List<Double> getPoint() {
        return point;
    }
}
