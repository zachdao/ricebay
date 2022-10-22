package edu.rice.comp610.controller;

import edu.rice.comp610.model.SearchSortField;
import edu.rice.comp610.util.JsonStatusResponse;

/**
 * Controller that handles requests for creating, updating, searching and viewing auctions
 */
public class AuctionController {

    /**
     * Searches for auctions matching query string and returns matching results.
     * @param query the query string to match against fields in the auction.
     * @param sortField the field to sort by
     * @param sortAscending if true, sorts in ascending order by sortField; otherwise descending order.
     * @return result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    JsonStatusResponse search(String query, SearchSortField sortField, boolean sortAscending) {
        return new JsonStatusResponse(true, null, "OK");
    }
}
