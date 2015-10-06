package com.excilys.voisinsenor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by mada on 09/09/15.
 */
// juste for Design my App
@JsonIgnoreProperties({"templateTag", "templateName", "customIcon", "targets",
        "webappResponse", "backend", "customImages", "customTheme", "website"})
public class AppModel {

    @JsonProperty("POIs")
    private List<POI> POIs;

    public List<POI> getPOIs() {
        return POIs;
    }

    public void setPOIs(List<POI> POIs) {
        this.POIs = POIs;
    }
}
