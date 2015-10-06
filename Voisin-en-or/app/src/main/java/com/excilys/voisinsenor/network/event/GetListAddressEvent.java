package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.Address;

import java.util.List;

/**
 * Created by mada on 10/09/15.
 */
public class GetListAddressEvent {
    List<Address> addresses;

    public GetListAddressEvent(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
