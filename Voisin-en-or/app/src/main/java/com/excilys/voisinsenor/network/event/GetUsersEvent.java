package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.User;

import java.util.List;

/**
 * Created by mada on 14/09/15.
 */
public class GetUsersEvent {

    private List<User> users;

    public GetUsersEvent(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
