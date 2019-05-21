package br.edu.insper.al.gustavobb.truckpad_insper;

import java.util.List;

public class GeoRouteReceived {
    private List<GeoPoint> points;
    private int distance;
    private String distance_unit;
    private int duration;
    private String duration_unit;
    private boolean has_tolls;
    private int toll_count;
    private float toll_cost;
    private String toll_cost_unit;
    private List<List<List<Double>>> route;
    private String provider;
    private boolean cached;
    private float fuel_usage;
    private String fuel_usage_unit;
    private float fuel_cost;
    private String fuel_cost_unit;
    private float total_cost;

    public List<GeoPoint> getPoints() {
        return points;
    }

    public int getDistance() {
        return distance;
    }

    public String getDistance_unit() {
        return distance_unit;
    }

    public int getDuration() {
        return duration;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public boolean isHas_tolls() {
        return has_tolls;
    }

    public int getToll_count() {
        return toll_count;
    }

    public float getToll_cost() {
        return toll_cost;
    }

    public String getToll_cost_unit() {
        return toll_cost_unit;
    }

    public List<List<List<Double>>> getRoute() {
        return route;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isCached() {
        return cached;
    }

    public float getFuel_usage() {
        return fuel_usage;
    }

    public String getFuel_usage_unit() {
        return fuel_usage_unit;
    }

    public float getFuel_cost() {
        return fuel_cost;
    }

    public String getFuel_cost_unit() {
        return fuel_cost_unit;
    }

    public float getTotal_cost() {
        return total_cost;
    }
}
