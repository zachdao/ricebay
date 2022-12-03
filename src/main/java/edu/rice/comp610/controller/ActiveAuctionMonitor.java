package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;

import java.util.*;

public class ActiveAuctionMonitor implements Runnable{
    private final AuctionManager auctionManager;

    public ActiveAuctionMonitor(AuctionManager AuctionManager){
        this.auctionManager = AuctionManager;
    }

    /**
     * Change attribute "published" to false in list of Auction objects
     *
     * @param expiredAuctions - a list of expired auctions
     */
    private void unpublish(List<Auction> expiredAuctions) throws BadRequestException, DatabaseException {
        for (Auction auction : expiredAuctions) {
            auction.setPublished(false);
            this.auctionManager.save(auction);
        }
    }


    public void doExpiryCheck() throws BadRequestException, DatabaseException {
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
        } catch (BadRequestException e) {
            e.printStackTrace();
            System.err.println("Encountered a BadRequestException while processing expired auctions");
        } catch (DatabaseException e) {
            e.printStackTrace();
            System.err.println("Encountered a DatabaseException while processing expired auctions");
        }
    }
}
