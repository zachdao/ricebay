package edu.rice.comp610.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a bid on an {@link Auction}.
 *
 * Includes a timestamp when the bid was placed, the amount of the bid, the bidder (ownerId), and the auction upon
 * which this bid was placed.
 */
public class Bid {
    private Date timestamp;
    private int amount;
    private UUID ownerId;
    private UUID auctionId;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(UUID auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return amount == bid.amount && Objects.equals(timestamp, bid.timestamp) && Objects.equals(ownerId, bid.ownerId) && Objects.equals(auctionId, bid.auctionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, amount, ownerId, auctionId);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "timestamp=" + timestamp +
                ", amount=" + amount +
                ", ownerId=" + ownerId +
                ", auctionId=" + auctionId +
                '}';
    }
}
