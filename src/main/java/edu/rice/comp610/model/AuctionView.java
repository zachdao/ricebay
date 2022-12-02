package edu.rice.comp610.model;


import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents the list of recently viewed auction items on an {@link Auction}.
 *
 * Includes a timestamp when the item was viewed and who viewed it.
 */
public class AuctionView {
    private UUID id;
    private Date timestamp;
    private UUID auctionId;
    private UUID viewerId;

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

    public UUID getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(UUID auctionId) {
        this.auctionId = auctionId;
    }

    public UUID getViewerId() {
        return viewerId;
    }

    public void setViewerUD(UUID viewerId) {
        this.viewerId = viewerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionView auctionView = (AuctionView) o;
        return Objects.equals(id, auctionView.id)
                && Objects.equals(timestamp, auctionView.timestamp)
                && Objects.equals(auctionId, auctionView.auctionId)
                && Objects.equals(viewerId, auctionView.viewerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, auctionId, viewerId);
    }


    @Override
    public String toString() {
        return "ItemView{" +
                "id=" + id +
                "timestamp=" + timestamp +
                ", auctionId=" + auctionId +
                ", viewId=" + viewerId +
                '}';
    }

}
