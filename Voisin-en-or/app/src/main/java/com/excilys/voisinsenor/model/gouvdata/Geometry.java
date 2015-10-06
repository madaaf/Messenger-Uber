package com.excilys.voisinsenor.model.gouvdata;

import java.util.List;

/**
 * Created by excilys on 14/08/15.
 */

/**
 * WARNING: Longitude is at position 0, latitude at position 1
 */
public class Geometry {

    List<Double> coordinates;

    /**
     * WARNING: Longitude is at position 0, latitude at position 1
     */
    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
