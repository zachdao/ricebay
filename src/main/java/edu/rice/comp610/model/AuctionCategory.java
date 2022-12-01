package edu.rice.comp610.model;

import edu.rice.comp610.store.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an auction category (auction_category table entry) on an {@link Auction}.
 *
 * This is a mapper table that just includes the auction_id and the category_id
 */
public class AuctionCategory {
    private UUID auctionId;
    private int categoryId;

    @PrimaryKey
    public UUID getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(UUID auctionId) {
        this.auctionId = auctionId;
    }

    @PrimaryKey
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionCategory auctionCategory = (AuctionCategory) o;
        return Objects.equals(auctionId, auctionCategory.auctionId)
                && Objects.equals(categoryId, auctionCategory.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, categoryId);
    }

    @Override
    public String toString() {
        return "AuctionCategory{" +
                "auction_id=" + auctionId +
                "categoryId=" + categoryId +
                '}';
    }
}
