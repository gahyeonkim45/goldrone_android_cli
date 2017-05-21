package com.test.kosta.goldrone_userapplication.DB;

/**
 * Created by kosta on 2016-07-14.
 */
public class DroneGPS {

    int node_id;
    int id;
    double lat,lon;

    public DroneGPS() {
    }

    public DroneGPS( int id, double lat, double lon){
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public DroneGPS(int node_id, int id, double lat, double lon) {
        this.node_id = node_id;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public int getNode_id() {
        return node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
