package com.excilys.voisinsenor.network.event;

/**
 * Created by mada on 10/09/15.
 */
public class GetCoordAddressEvent {

    double lat;
    double lgn;

    public GetCoordAddressEvent(double lat, double lgn) {
        this.lat = lat;
        this.lgn = lgn;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLgn() {
        return lgn;
    }

    public void setLgn(double lgn) {
        this.lgn = lgn;
    }
}
