package edu.rice.comp610.store;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;

import java.util.List;
import java.util.UUID;

/**
 * Manages interactions with the database.
 */
public class DatabaseManager {

    /**
     * Search for auctions based on the specified {@link AuctionQuery}.
     * @param query the query parameters to search for.
     * @return a list of matching {@link Auction}s
     */
    public List<Auction> searchAuctions(AuctionQuery query) {
        return null;
    }

    /**
     * Save a user's account information. If the accountId field is null, then this method creates a new account. If
     * the accountId is set, then it updates the account with the given ID.
     * @param account the account to save.
     */
    public void saveAccount(Account account) {

    }

    /**
     * Loads the account information of a user with the given alias. If a user with the given alias does not exist,
     * then throws an exception.
     * @param alias the alias of the account to load.
     * @throws ObjectNotFoundException if the account is not found
     */
    public Account loadAccount(String alias) throws ObjectNotFoundException {
        return null;
    }

    /**
     * Saves the given auction. If the auctionId field is null, then this method creates a new auction. If
     * the auctionId is set, then it updates the auction with the given ID.
     * @param auction the auction to save.
     */
    public void saveAuction(Auction auction) {
    }

    /**
     * Loads the auction with the given ID. If an auction with the given ID does not exist,
     * then throws an exception.
     * @param auctionId the ID of the auction to load.
     * @throws ObjectNotFoundException if the auction is not found
     */
    public Auction loadAuction(UUID auctionId) throws ObjectNotFoundException {
        return null;
    }
}
