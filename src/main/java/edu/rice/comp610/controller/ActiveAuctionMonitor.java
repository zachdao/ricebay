package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ActiveAuctionMonitor implements Runnable{
    private AuctionManager auctionManager;
    private boolean isRunning;
    public ActiveAuctionMonitor(AuctionManager AuctionManager){
        this.auctionManager = AuctionManager;
        this.isRunning = false;
    }

    /**
     * Method returns list of auctions where attribute endDate is less than current time
     *
     */

    public List<Auction> getExpired(){
        // SQL with condition auction.endDate < currentTime goes here
        System.out.println("SQL query performed! This will be replaced by proper SQL query...");
        return new ArrayList<>();
    }

    /**
     * Change attribute "published" to false in list of Auction objects
     *
     * @param expiredAuctions - a list of expired auctions
     */
    public void unpublish(List<Auction> expiredAuctions) throws BadRequestException, DatabaseException {
        for (Auction auction : expiredAuctions) {
            auction.setPublished(false);
            this.auctionManager.save(auction);
        }
    }


    public void doExpiryCheck() throws BadRequestException, DatabaseException {
        List<Auction> expiredAuctions = getExpired();
        unpublish(expiredAuctions);
    }

    /**
     * Performs expiry check on all auctions in database with given frequency (currently set to 1 min).
     *
     * returns result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */

    public void run() {
        while (true) {

            if (isRunning) {
                System.err.println("Run already called!");
                return;
            }
            isRunning = true;
            while (isRunning) {
                try {
                    doExpiryCheck();
                } catch (BadRequestException e) {
                    e.printStackTrace();
                    System.err.println("Encountered a BadRequestException while processing expired auctions");
                    isRunning = false;
                } catch (DatabaseException e) {
                    e.printStackTrace();
                    System.err.println("Encountered a DatabaseException while processing expired auctions");
                    isRunning = false;
                }
                System.out.println("ACTIVE AUCTION MONITOR will sleep now for 1 min");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    System.out.println("Exiting ActiveAuctionMonitor due to interrupt.");
                    isRunning = false;
                }
                System.out.println("Sleeping is done...");
                System.out.println();
            }
        }
    }
}
