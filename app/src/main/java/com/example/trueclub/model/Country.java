package com.example.trueclub.model;

public class Country {
    String countryId;
    String prob;

    public Country(String countryId, String prob) {
        this.countryId = countryId;
        this.prob = prob;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }
}
