package com.excilys.voisinsenor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by mada on 09/09/15.
 */
public class POI implements Serializable{

    @JsonProperty("lat")
    double lat;
    @JsonProperty("lng")
    double lng;
    @JsonProperty("name")
    String name;
    @JsonProperty("address")
    String address;

    public POI(){}

    public POI(String name, String address, double lat, double longitude) {
        this.lat = lat;
        this.name = name;
        this.address = address;
        this.lng = longitude;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
