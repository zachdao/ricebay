package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.store.AuctionQuery;
import edu.rice.comp610.store.ObjectNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Manages requests for creating, updating, searching and loading auctions.
 */
public class AuctionManager {

    /**
     * Creates a new auction in the system. A new id is assigned to the auction by the system.
     *
     * @param auction the auction to create
     * @return a response containing an new auction with its id field filled in, or an error message if an error
     * occurred.
     */
    AppResponse<UUID> createAuction(Auction auction) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     * Updates an existing auction. The id field must be that of an existing auction.
     *
     * @param auction the auction to update
     * @return a response with the status of the update; if an error occurred, the response will include an error
     * message.
     */
    AppResponse<Void> updateAuction(Auction auction) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     * Loads an existing auction and returns it. The id must be that of an existing auction.
     *
     * @param auctionId the id of auction to load
     * @return a response containing the auction; or an error message, if an error occurred (e.g., the auction could not
     * be found).
     */
    AppResponse<Auction> loadAuction(UUID auctionId) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     * Searches for auctions matching query string and returns matching results.
     *
     * @param query the {@link AuctionQuery} object to match against fields in the auction.
     * @return result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    AppResponse<List<Auction>> search(AuctionQuery query) {
        return new AppResponse<>(true, null, "OK");
    }
}
