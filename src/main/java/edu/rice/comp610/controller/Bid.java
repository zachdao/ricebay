package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;

import java.util.UUID;

public class Bid {

    /**
     * When a user is registering for the website, they will be shown a page where they can enter basic information
     * about themselves such as their username, first and last name, their email, password, a profile photo, and their
     * Zelle ID. An error will be returned if the email address does not end in ‘@rice.edu’ or if the email is already
     * associated with an account. If neither of these scenarios occur, this information will be sent to the database
     * and stored.
     *
     * @param bid the bid information.
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see Bid
     */
    public AppResponse<Account> saveBid(Bid bid) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     *
     * @param bid_id the bid id name
     * @return a response object containing the corresponding bid information,
     * (timestamp, amount, owner_id, auction_id) or null if the account does not exist.
     */
    public AppResponse<Account> retrieveBid(UUID bid_id) {
        return new AppResponse<>(true, null, "OK");
    }

}
