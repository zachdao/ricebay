package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.store.AuctionQuery;
import edu.rice.comp610.store.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages requests for creating, updating, searching and loading auctions.
 */
public class AuctionManager {

    private final DatabaseManager databaseManager;

    public AuctionManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Creates a new auction in the system. A new id is assigned to the auction by the system.
     *
     * @param auction the auction to create
     * @return a response containing a new auction with its id field filled in, or an error message if an error
     * occurred.
     */
    AppResponse<UUID> createAuction(Auction auction) {
        // TODO: use the QueryManager to construct queries.
        List<Account> accounts = databaseManager.loadObjects(Account.class,
                "SELECT * FROM account WHERE id = ?", auction.getOwnerId());
        if (accounts.isEmpty()) {
            return new AppResponse<>(false, null, "Account ID " + auction.getOwnerId()
                    + " does not exist");
        }

        List<Category> categories = new ArrayList<>();
        // TODO: querying one object at a time in a loop is inefficient; refactor to use one query.
        for (UUID id : auction.getCategoryIds()) {
            List<Category> tmp = databaseManager.loadObjects(Category.class,
                    "SELECT * FROM category WHERE id = ?", id);
            if (tmp.isEmpty()) {
                return new AppResponse<>(false, null, "Category ID " + id + " does not exist");
            }
            categories.addAll(tmp);
        }

        auction.setId(UUID.randomUUID());
        databaseManager.saveObjects("INSERT INTO auction () VALUES ()", auction.getClass().getFields(), auction);

        return new AppResponse<>(true, auction.getId(), "OK");
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
