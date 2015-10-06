package com.excilys.voisinsenor.model;

/**
 * Created by mada on 10/09/15.
 */
public class Registration {
    private String email;
    private String registrationId;

    public Registration(String email, String registrationId) {
        this.email = email;
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
