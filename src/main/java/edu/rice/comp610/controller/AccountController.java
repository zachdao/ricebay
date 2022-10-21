package edu.rice.comp610.controller;

import edu.rice.comp610.util.JsonStatusResponse;

/**
 * Controller that handles incoming requests for creating, viewing and updating accounts in the RiceBay system.
 */
public class AccountController {

    /**
     * When a user is registering for the website, they will be shown a page where they can enter basic information
     * about themselves such as their username, first and last name, their email, password, a profile photo, and their
     * Zelle ID. An error will be returned if the email address does not end in ‘@rice.edu’ or if the email is already
     * associated with an account. If neither of these scenarios occur, this information will be sent to the database
     * and stored.
     *
     * @param alias the user's chosen login name (must be unique in the system)
     * @param email the user's email address (must be unique in the system)
     * @param givenName the user's given name
     * @param surname the user's surname
     * @param password the password selected by the user
     * @param zelleId the user's Zelle ID
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     */
    JsonStatusResponse createAccount(
            String alias,
            String email,
            String givenName,
            String surname,
            String password,
            String zelleId
    ) {
        return new JsonStatusResponse(true, null, "OK");
    }
}
