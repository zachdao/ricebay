package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;

import java.util.UUID;

/**
 * Updates the sellers rating
 */
public class RatingManager {

    /**
     * Retrieves information about a user account
     *
     * @param raterId the rater's ID
     * @param sellerId the sellers ID
     * @param rating the rating of the user
     * @return a response with the status of the rating attempt, or null if either of the accounts do not exist.
     */
    public AppResponse<Account> retrieveAccount(UUID raterId, UUID sellerId, int rating) {
        return new AppResponse<>(true, null, "OK");
    }


}
