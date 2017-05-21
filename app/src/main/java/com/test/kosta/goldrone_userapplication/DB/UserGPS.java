package com.test.kosta.goldrone_userapplication.DB;

/**
 * Created by kosta on 2016-07-14.
 */
public class UserGPS {

    double lat,lon;

    public UserGPS() {
    }

    public UserGPS(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
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
