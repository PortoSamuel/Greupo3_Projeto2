package br.edu.insper.truckpad_insper;

public class PriceInformation {
    private int axis;
    private double distance;
    private boolean has_return_shipment;
    private String type;

    public PriceInformation(int axis, double distance, boolean has_return_shipment, String type){
        this.axis = axis;
        this.distance = distance;
        this.has_return_shipment = has_return_shipment;
        this.type = type;
    }
}
