package edu.rice.comp610.controller;

import java.util.UUID;

public class ViewAccount {
    private UUID id;
    private String alias;
    private String email;
    private String givenName;
    private String surname;
    private String image;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getZelleId() {
        return zelleId;
    }

    public void setZelleId(String zelleId) {
        this.zelleId = zelleId;
    }

    private String zelleId;
}
