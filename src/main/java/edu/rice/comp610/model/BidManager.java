package edu.rice.comp610.model;

import edu.rice.comp610.controller.AppResponse;
import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Bid;
import edu.rice.comp610.store.DatabaseException;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * Controller that handles incoming requests for creating and viewing bids in the RiceBay system.
 */
public class BidManager {

    /**
     * Bid manager, handles adding new bids to the database and retrieving bids from the database
     */

    private final Bid bid;
    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public BidManager(Bid bid, QueryManager queryManager, DatabaseManager databaseManager){
        this.bid = bid;
        this.queryManager = queryManager;
        this.databaseManager = databaseManager;
    }

    /**
     * This method finds bid with highest values form list of Bids objects
     */
    public Double findMaxBid(List<Bid> allBidsForAuction){

        ListIterator<Bid> iterator = allBidsForAuction.listIterator();
        Double maxBid = 0.0;
        while (iterator.hasNext()){
            Double current_value = iterator.next().getAmount();
            if (current_value > maxBid){
                maxBid = current_value;
            }
        }
        return maxBid;
    }


    /**
     * This methods search in DB for all bids that have same Auction_ID. It traverses through list af all found
     * Bid objects and finds maximum bid value
     */
    public AppResponse<Double> findCurrentBid(Bid bid){
        UUID auctionID = bid.getAuctionId();
        try{
            Query<Bid> loadBid = queryManager.makeLoadQuery(Bid.class, "auctionId");
            List<Bid> allBidsForAuction = databaseManager.loadObjects(loadBid, auctionID);

            if (allBidsForAuction.isEmpty()) {
                return new AppResponse<>(false, null, "Bid ID " + bid.getId()
                        + " does not exist");
            }

            Double maxBid = findMaxBid(allBidsForAuction);
            return new AppResponse<>(true, maxBid, "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method finds in DB Auction that Bid is part of
     * It returns bid increment for that Auction object
     */
    public AppResponse<Double> findBidIncrement(Bid bid) {
        UUID auctionID = bid.getAuctionId();
        try {
            Query<Auction> loadAuction = queryManager.makeLoadQuery(Auction.class, "id");
            List<Auction> auction = databaseManager.loadObjects(loadAuction, auctionID);

            if (auction.isEmpty()) {
                return new AppResponse<>(false, null, "Auction ID" + bid.getAuctionId()
                        + " does not exists");
            }
            ListIterator<Auction> iterator = auction.listIterator();
            double minIncrement = iterator.next().getBidIncrement();

            return new AppResponse<>(true, minIncrement, "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is finding Auction object in DB and is returning UUID of Auction Owner
     */
    public AppResponse<UUID> getAuctionOwnerByBid(Bid bid){
        UUID auctionID = bid.getAuctionId();
        try{
            Query<Auction> loadAuction = queryManager.makeLoadQuery(Auction.class, "id");
            List<Auction> auction = databaseManager.loadObjects(loadAuction, auctionID);

            if (auction.isEmpty()) {
                return new AppResponse<>(false, null, "Auction ID" + bid.getAuctionId()
                        + " does not exists");
            }
            ListIterator<Auction> iterator = auction.listIterator();
            UUID auctionOwner = iterator.next().getOwnerId();

            return new AppResponse<>(true, auctionOwner, "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This methods checks if bid satisfies following conditions:
     * 1. new bid > current bid + bid increment
     * 2. Bidder is not same User who is owner of Auction
     */
    public boolean checkBidConformity (Bid bid){

        AppResponse<Double> responseCurrentBid = findCurrentBid(bid);
        AppResponse<Double> responseBidIncrement = findBidIncrement(bid);
        AppResponse<UUID> responseAuctionOwnerUUID = getAuctionOwnerByBid(bid);

        System.out.println("Current bid is: " + responseCurrentBid.getData());
        System.out.println("Increment is: " + responseBidIncrement.getData());
        System.out.println("Auction owner is: " + responseAuctionOwnerUUID.getData());

        // check conditions:
        // 1. new_bid > current_bid + bid_increment
        // 2. Owner of bid is not Owner of Auction
        if ((bid.getAmount() > responseCurrentBid.getData() + responseBidIncrement.getData())
                && !(bid.getOwnerId().toString() == responseAuctionOwnerUUID.toString())){
            return true;
        }else {return false;}
    }

    /**
     * This method saves Bid object to DB if bid conformity is confirmed
     */
    public AppResponse<UUID> saveBid(Bid bid) {

        //STEPS:
        // 1. Check bids confomrity (new_bid >  old_bid + min incremenet)
        // 2. Check if Bidder is not owner of Auction
        // 2. If bid is ok do Save Bid

        boolean bidConformity = checkBidConformity(bid);

        if (bidConformity) {
            try {
                Query<Bid> saveQuery = queryManager.makeUpdateQuery(Bid.class);
                databaseManager.saveObjects(saveQuery, bid);

                return new AppResponse<>(true, bid.getId(), "OK");
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }else{
            return new AppResponse<>(false, bid.getId(), "NOT OK");
        }
    }

    /**
     * This method retrieves Bids from DB
     */
    public AppResponse<Account> retrieveBid(UUID bidId) {
        return new AppResponse<>(true, null, "OK");
    }

}
