package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Bid;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;

import java.util.*;

public class ActiveAuctionMonitor implements Runnable{
    private final AuctionManager auctionManager;
    private final BidManager bidManager;

    public ActiveAuctionMonitor(AuctionManager auctionManager, BidManager bidManager){
        this.auctionManager = auctionManager;
        this.bidManager = bidManager;
    }

    /**
     * Updates expired auctions to be unpublished and to declare the winner
     *
     * @param expiredAuctions - a list of expired auctions
     */
    private void unpublish(List<Auction> expiredAuctions) {
        expiredAuctions.parallelStream().forEach(auction -> {
            try {
                auction.setPublished(false);
                Bid winningBid = this.bidManager.getCurrentBid(auction.getId());
                auction.setWinnerId(winningBid.getOwnerId());
                this.auctionManager.save(auction);
            } catch (DatabaseException | BadRequestException e) {
                System.err.println("Failed to update auction with winner and set to unpublished");
            }
        });
    }

    public void doExpiryCheck() throws DatabaseException {
        System.out.println("Getting expired auctions");
        List<Auction> expiredAuctions = this.auctionManager.expired();
        System.out.format("Un-publishing %d auctions\n", expiredAuctions.size());
        unpublish(expiredAuctions);
    }

    /**
     * Performs expiry check on all auctions in database with given frequency (currently set to 1 min).
     * returns result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    public void run() {
        try {
            doExpiryCheck();
        } catch (DatabaseException e) {
            e.printStackTrace();
            System.err.println("Encountered a DatabaseException while processing expired auctions");
        }
    }
}
