package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;

/**
 * Controller that handles incoming requests for creating, viewing and updating accounts in the RiceBay system.
 */
public class UserManager {

    /**
     * When a user is registering for the website, they will be shown a page where they can enter basic information
     * about themselves such as their username, first and last name, their email, password, a profile photo, and their
     * Zelle ID. An error will be returned if the email address does not end in ‘@rice.edu’ or if the email is already
     * associated with an account. If neither of these scenarios occur, this information will be sent to the database
     * and stored.
     *
     * @param account the user's account information.
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see Account
     */
    public AppResponse<Account> saveAccount(Account account) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     *
     * @param alias the user's login name
     * @return a response object containing the corresponding user account, or null if the account does not exist.
     */
    public AppResponse<Account> retrieveAccount(String alias) {
        return new AppResponse<>(true, null, "OK");
    }
}