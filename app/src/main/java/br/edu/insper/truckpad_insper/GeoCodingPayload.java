package br.edu.insper.truckpad_insper;

import java.util.LinkedList;
import java.util.List;

public class GeoCodingPayload {
//    private List<GeoPlace> places = new LinkedList<>();
    private GeoPlace[] places = new GeoPlace[2];
    private int vehicle_type;
    private float fuel_consumption;
    private float fuel_price;

    public void setVehicle_type(int type){this.vehicle_type = type;}

    public void setFuel_consumption(float consumption){this.fuel_consumption = consumption;}

    public void setFuel_price(float price){this.fuel_price = price;}

    public void putPlaceOrigin(GeoPlace place){
//        this.places.add(0, place);
        places[0] = place;
    }

    public void putPlaceDestiny(GeoPlace place){
//        this.places.add(1, place);
        places[1] = place;
    }

    public void deletePlaceOrigin(){
//        this.places.remove(0);
        places[0] = null;
    }

    public void deletePlaceDestiny(){
//        this.places.remove(1);
        places[1] = null;
    }

    public List<GeoPlace> getPlaces(){
        List<GeoPlace> placesR = new LinkedList<>();
        placesR.add(0, places[0]);
        placesR.add(1, places[1]);
        return placesR;
    }

}
