package edu.rice.comp610.model;

import edu.rice.comp610.controller.BidManager;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Bid manager, handles adding new bids to the database and retrieving bids from the database
 */
public class StandardBidManager implements BidManager {
    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public StandardBidManager(QueryManager queryManager, DatabaseManager databaseManager) {
        this.queryManager = queryManager;
        this.databaseManager = databaseManager;
    }

    /**
     * getCurrentBid methods search in DB for all bids that have same Auction_ID. It traverses through list af all found
     * Bid objects and finds maximum bid value
     */
    public Bid getCurrentBid(UUID auctionId) throws DatabaseException {
        var loadBid = queryManager.makeLoadQuery(Bid.class, queryManager.filters().makeEqualityFilter("auction_id"));
        List<Bid> allBidsForAuction = databaseManager.loadObjects(loadBid, auctionId);

        Bid maxBid = null;
        for (Bid bid : allBidsForAuction) {
            if (maxBid == null || bid.getAmount() >= maxBid.getAmount()) {
                maxBid = bid;
            }
        }

        return maxBid;
    }

    public List<Bid> getAuctionBids(UUID auctionId) throws DatabaseException {
        var loadBid = queryManager.makeLoadQuery(Bid.class, queryManager.filters().makeEqualityFilter("auction_id"));
        return databaseManager.loadObjects(loadBid, auctionId);
    }

    public Bid getUserBid(UUID auctionId, UUID ownerId) throws DatabaseException {
        var loadBid = queryManager.makeLoadQuery(Bid.class,
                queryManager.filters().makeAndFilter(
                    queryManager.filters().makeEqualityFilter("auction_id"),
                    queryManager.filters().makeEqualityFilter("owner_id")));
        List<Bid> bids = databaseManager.loadObjects(loadBid, auctionId, ownerId);

        return bids.isEmpty() ? null : bids.get(0);
    }

    /**
     * This methods checks if bid satisfies following conditions:
     * 1. new bid > current bid + bid increment
     * 2. Bidder is not same User who is owner of Auction
     */
    private boolean invalidBid(Auction auction, Bid bid) throws DatabaseException {
        Bid currentBid = this.getCurrentBid(auction.getId());

        // check conditions:
        // 1. new_bid > current_bid + bid_increment
        // 2. Owner of bid is not Owner of Auction
        boolean isHighestBid = currentBid == null || bid.getAmount() > currentBid.getAmount() + auction.getBidIncrement();
        boolean isOwner = bid.getOwnerId().equals(auction.getOwnerId());

        return !isHighestBid || isOwner;
    }

    /**
     * This method saves Bid object to DB if bid conformity is confirmed
     */
    public void placeBid(Auction auction, Bid bid) throws DatabaseException, BadRequestException {
        if(invalidBid(auction, bid)) {
            throw new BadRequestException("Invalid bid", Collections.singletonMap("bid", "Bid is invalid"));
        }

        bid.setAuctionId(auction.getId());
        var saveQuery = queryManager.makeUpdateQuery(Bid.class);
        databaseManager.saveObjects(saveQuery, bid);
    }

    public void updateBid(Auction auction, Bid bid) throws DatabaseException, ObjectNotFoundException, BadRequestException {
        if(invalidBid(auction, bid)) {
            throw new BadRequestException("Invalid bid", Collections.singletonMap("bid", "Bid is invalid"));
        }
        var loadQuery = queryManager.makeLoadQuery(Bid.class,
                queryManager.filters().makeAndFilter(
                    queryManager.filters().makeEqualityFilter("auction_id"),
                    queryManager.filters().makeEqualityFilter("owner_id")));
        List<Bid> bids = databaseManager.loadObjects(loadQuery, auction.getId(), bid.getOwnerId());
        if (bids.size() != 1) {
            throw new ObjectNotFoundException("Not Found");
        }

        bid.setAuctionId(auction.getId());
        bid.setId(bids.get(0).getId());
        bid.setMaxBid(bids.get(0).getMaxBid());
        var saveQuery = queryManager.makeUpdateQuery(Bid.class);
        databaseManager.saveObjects(saveQuery, bid);
    }

}
