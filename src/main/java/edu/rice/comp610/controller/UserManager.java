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
     * Retrieves information about a user account
     *
     * @param alias the user's login name
     * @return a response object containing the corresponding user account, or null if the account does not exist.
     */
    public AppResponse<Account> retrieveAccount(String alias) {
        return new AppResponse<>(true, null, "OK");
    }


    /**
     * Given the alias (username), compare the password saved in the database and return a success or failure based on success of login
     *
     * @param alias the user's account information.
     * @return a response with the status of the login attempt; if an error occurred, the response will include an error
     * message.
     */
    public AppResponse<Account> validateLogin(String alias) {return new AppResponse<>(true, null, "OK");}


    /**
     * Given the alias (username), destroy the user session
     *
     * @param alias the user's account information.
     * @return a response with the status of the session destruction; if an error occurred, the response will include an error
     * message.
     */
    public AppResponse<Account> logout(String alias) {return new AppResponse<>(true, null, "OK");}


}
