package br.edu.insper.al.gustavobb.truckpad_insper;

import java.util.LinkedList;
import java.util.List;

public class GeoCodingPayload {
    private List<GeoPlace> places = new LinkedList<>();
    private int vehicle_type;
    private float fuel_consumption;
    private float fuel_price;

    public void putPlace(GeoPlace place){
        this.places.add(place);
    }

    public List<GeoPlace> getPlaces() { return places; }

}
