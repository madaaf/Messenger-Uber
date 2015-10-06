package com.excilys.voisinsenor.model;

/**
 * Created by mada on 10/09/15.
 */
public class Address {

    private String address;
    private double lat;
    private double lng;

    public Address() {
    }

    public Address(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
