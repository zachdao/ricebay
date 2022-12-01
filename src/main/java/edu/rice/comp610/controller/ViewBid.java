package edu.rice.comp610.controller;

import java.util.Date;

public class ViewBid {
    private Double bid;
    private Double maxBid;
    private Date timestamp;

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
