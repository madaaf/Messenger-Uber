package com.excilys.voisinsenor.model.gouvdata;

import java.util.List;

/**
 * Deserialized address search result. Note this class and other classes in package gouvdata
 * are meant to be used with Gson.
 */
public class GeoCode {

    List<Features> features;

    public GeoCode() {
    }

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }
}
