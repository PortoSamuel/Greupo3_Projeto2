package br.edu.insper.truckpad_insper;

import java.util.LinkedList;
import java.util.List;

public class GeoCodingPayload {
    private List<GeoPlace> places = new LinkedList<>();
    private int vehicle_type;
    private float fuel_consumption;
    private float fuel_price;

    public void setVehicle_type(int type){this.vehicle_type = type;}

    public void setFuel_consumption(float consumption){this.fuel_consumption = consumption;}

    public void setFuel_price(float price){this.fuel_price = price;}

    public void putPlaceOrigin(GeoPlace place){
            this.places.add(place);
            System.out.println(places);

    }

    public void putPlaceDestiny(GeoPlace place){
            this.places.add(place);
            System.out.println(places);


    }

    public void deletePlaceOrigin(){
        this.places.remove(0);
    }

    public void deletePlaceDestiny(){
        this.places.remove(1);

    }

    public List<GeoPlace> getPlaces() { return places; }

}
