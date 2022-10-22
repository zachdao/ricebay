package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.store.AuctionQuery;
import edu.rice.comp610.store.AuctionSortField;

import java.util.List;

/**
 * Controller that handles requests for creating, updating, searching and viewing auctions
 */
public class AuctionController {

    /**
     * Searches for auctions matching query string and returns matching results.
     * @param query the {@link AuctionQuery} object to match against fields in the auction.
     * @return result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    AppResponse<List<Auction>> search(AuctionQuery query) {
        return new AppResponse<>(true, null, "OK");
    }
}
