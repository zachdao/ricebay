package edu.rice.comp610.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ViewAuction {
    private UUID id;
    private List<String> categories;
    private String title;
    private String description;
    private double minimumBid;
    private double bidIncrement;
    private double currentBid;
    private Date startDate;
    private Date endDate;
    private double taxPercent;
    private boolean published;
    private ViewSeller seller;
    private ViewBid userBid;
    private List<String> images;
    private List<ViewBid> bids;
    private ViewWinner winner;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(double minimumBid) {
        this.minimumBid = minimumBid;
    }

    public double getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(double bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public ViewSeller getSeller() {
        return seller;
    }

    public void setSeller(ViewSeller seller) {
        this.seller = seller;
    }

    public ViewBid getUserBid() {
        return userBid;
    }

    public void setUserBid(ViewBid userBid) {
        this.userBid = userBid;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ViewBid> getBids() {
        return bids;
    }

    public void setBids(List<ViewBid> bids) {
        this.bids = bids;
    }

    public ViewWinner getWinner() {
        return winner;
    }

    public void setWinner(ViewWinner winner) {
        this.winner = winner;
    }
}
