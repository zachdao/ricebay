package edu.rice.comp610.model;

import edu.rice.comp610.store.OneToMany;
import edu.rice.comp610.store.PrimaryKey;
import edu.rice.comp610.store.SqlType;
import org.postgresql.util.PGmoney;

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
    private double minimumBid;
    private double bidIncrement;
    private Date startDate;
    private Date endDate;
    private double taxPercent;
    private boolean published;

    @PrimaryKey
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

    @OneToMany(table="category", on="acution_id")
    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
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

    @SqlType(PGmoney.class)
    public double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(double minimumBid) {
        this.minimumBid = minimumBid;
    }

    @SqlType(PGmoney.class)
    public double getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(double bidIncrement) {
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

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public boolean getPublished() {
        return published;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(categoryIds, auction.categoryIds)
                && minimumBid == auction.minimumBid
                && bidIncrement == auction.bidIncrement
                && Double.compare(auction.taxPercent, taxPercent) == 0
                && Objects.equals(id, auction.id)
                && Objects.equals(ownerId, auction.ownerId)
                && Objects.equals(title, auction.title)
                && Objects.equals(description, auction.description)
                && Objects.equals(startDate, auction.startDate)
                && Objects.equals(endDate, auction.endDate)
                && published == auction.published;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, categoryIds, title, description, minimumBid, bidIncrement, startDate, endDate, taxPercent, published);
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
                ", taxPercentage=" + taxPercent +
                ", published=" + published +
                '}';
    }
}
