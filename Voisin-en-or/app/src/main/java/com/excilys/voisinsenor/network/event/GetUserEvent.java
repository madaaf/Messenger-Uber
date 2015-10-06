package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.User;

/**
 * Created by mada on 21/09/15.
 */
public class GetUserEvent {

    private User user;

    public GetUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
