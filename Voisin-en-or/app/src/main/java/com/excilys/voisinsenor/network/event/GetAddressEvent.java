package com.excilys.voisinsenor.network.event;

/**
 * Created by mada on 10/09/15.
 */
public class GetAddressEvent {
    String address;

    public GetAddressEvent(String address){
        this .address = address;
    }

    public String getAddress(){ return address;}
}
