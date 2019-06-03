package br.edu.insper.truckpad_insper.api.payloads;

import java.util.LinkedList;
import java.util.List;

public class GeoCodingPayload {
    private List<GeoPlace> places = new LinkedList<>();
    private int vehicle_type;
    private float fuel_consumption;
    private float fuel_price;

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
