package edu.rice.comp610.model;

import java.util.*;

/**
 * An auction listing in the RiceBay system.
 *
 * <p>Represents the item listed, including title, description, photos, bid settings, start and end dates.</p>
 */
public class Auction {
    private UUID id;
    private UUID ownerId;
    private List<Integer> categoryIds;
    private String title;
    private String description;
    private int minimumBid;
    private int bidIncrement;
    private Date startDate;
    private Date endDate;
    private float salesTaxRate;
    private AuctionState state;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryId(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
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

    public int getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(int minimumBid) {
        this.minimumBid = minimumBid;
    }

    public int getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(int bidIncrement) {
        this.bidIncrement = bidIncrement;
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

    public float getSalesTaxRate() {
        return salesTaxRate;
    }

    public void setSalesTaxRate(float salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(categoryIds, auction.categoryIds)
                && minimumBid == auction.minimumBid
                && bidIncrement == auction.bidIncrement
                && Float.compare(auction.salesTaxRate, salesTaxRate) == 0
                && Objects.equals(id, auction.id)
                && Objects.equals(ownerId, auction.ownerId)
                && Objects.equals(title, auction.title)
                && Objects.equals(description, auction.description)
                && Objects.equals(startDate, auction.startDate)
                && Objects.equals(endDate, auction.endDate)
                && state == auction.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, categoryIds, title, description, minimumBid, bidIncrement, startDate, endDate, salesTaxRate, state);
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", categoryId=" + categoryIds +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", minimumBid=" + minimumBid +
                ", bidIncrement=" + bidIncrement +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", salesTaxRate=" + salesTaxRate +
                ", state=" + state +
                '}';
    }
}
