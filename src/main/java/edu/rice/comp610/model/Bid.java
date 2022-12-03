package edu.rice.comp610.model;

import edu.rice.comp610.store.PrimaryKey;
import edu.rice.comp610.store.SqlType;
import org.postgresql.util.PGmoney;

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
    private UUID id;
    private Date timestamp;
    private Double amount;
    private UUID ownerId;
    private UUID auctionId;

    private Double maxBid;

    @PrimaryKey
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @SqlType(PGmoney.class)
    public Double getAmount() {
        return amount;
    }

    @SqlType(PGmoney.class)
    public void setAmount(Double amount) {
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

    @SqlType(PGmoney.class)
    public Double getMaxBid() {
        return maxBid;
    }

    @SqlType(PGmoney.class)
    public void setMaxBid(Double maxBid) {
        this.maxBid = maxBid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id)
                && Objects.equals(amount, bid.amount)
                && Objects.equals(timestamp, bid.timestamp)
                && Objects.equals(ownerId, bid.ownerId)
                && Objects.equals(auctionId, bid.auctionId)
                && Objects.equals(maxBid, bid.maxBid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, amount, ownerId, auctionId);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                "timestamp=" + timestamp +
                ", amount=" + amount +
                ", ownerId=" + ownerId +
                ", auctionId=" + auctionId +
                ", maxBid=" + maxBid +
                '}';
    }
}
