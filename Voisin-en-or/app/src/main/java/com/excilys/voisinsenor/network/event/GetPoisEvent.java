package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.POI;

import java.util.List;

/**
 * Created by mada on 09/09/15.
 */
public class GetPoisEvent {
    private List<POI> POIs;

    public GetPoisEvent(List<POI> POIs){
        this.POIs = POIs;
    }

    public List<POI> getPOIs(){ return  POIs;}
}
