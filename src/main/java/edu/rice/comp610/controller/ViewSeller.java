package edu.rice.comp610.controller;

public class ViewSeller {
    private String alias;
    private Double rating;

    private Double yourRating;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getYourRating() {
        return yourRating;
    }

    public void setYourRating(Double yourRating) {
        this.yourRating = yourRating;
    }
}
