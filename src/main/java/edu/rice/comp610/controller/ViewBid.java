package edu.rice.comp610.controller;

import java.util.Date;

public class ViewBid {
    private String alias;
    private Double bid;
    private Double maxBid;
    private Date timestamp;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(Double maxBid) {
        this.maxBid = maxBid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
