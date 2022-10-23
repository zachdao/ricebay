package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;

import java.util.UUID;

/**
 * Controller that handles incoming requests for creating and viewing bids in the RiceBay system.
 */
public class BidManager {

    /**
     * Bid manager, handles adding new buds to the database and retrieving bids from the database
     *
     * @param bid the bid information.
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see BidManager
     */
    public AppResponse<Account> saveBid(BidManager bid) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     *
     * @param bidId the bid id name
     * @return a response object containing the corresponding bid information,
     * (timestamp, amount, owner_id, auction_id) or null if the account does not exist.
     */
    public AppResponse<Account> retrieveBid(UUID bidId) {
        return new AppResponse<>(true, null, "OK");
    }

}
