package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.store.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages requests for creating, updating, searching and loading auctions.
 */
public class AuctionManager {

    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public AuctionManager(QueryManager queryManager, DatabaseManager databaseManager) {
        this.queryManager = queryManager;
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
        try {
            // TODO: use the QueryManager to construct queries.
            Query<Account> accountQuery = queryManager.makeLoadQuery(Account.class, "id");
            List<Account> accounts = databaseManager.loadObjects(accountQuery, auction.getOwnerId());
            if (accounts.isEmpty()) {
                return new AppResponse<>(false, null, "Account ID " + auction.getOwnerId()
                        + " does not exist");
            }

            // TODO: querying one object at a time in a loop is inefficient; refactor to use one query.
            Query<Category> categoryQuery = queryManager.makeLoadQuery(Category.class, "id");
            for (int id : auction.getCategoryIds()) {
                List<Category> tmp = databaseManager.loadObjects(categoryQuery, id);
                if (tmp.isEmpty()) {
                    return new AppResponse<>(false, null, "Category ID " + id + " does not exist");
                }
            }

            auction.setId(UUID.randomUUID());
            Query<Auction> auctionQuery = queryManager.makeUpdateQuery(Auction.class);
            databaseManager.saveObjects(auctionQuery, auction);

            return new AppResponse<>(true, auction.getId(), "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
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
        // TODO: Replace with real query, this is dummy data for getting frontend working
        ArrayList<Auction> dummyAuctions = new ArrayList<>();
        for (int i = 0; i < 90; i++) {
            var auction = new Auction();
            auction.setId(UUID.randomUUID());
            auction.setTitle(String.format("Foo %d", i));
            auction.setMinimumBid(((int) Math.floor(Math.random() * 150 + 1)));
            if (i % 5 == 0) {
                auction.setDescription("some longer foo stuff aba abawsdbg abasdfasd asdfasdfasdd asdfasdfas dfasdfasd fasdf asd fasdf asd fas df asdf asdf asdf asd f asdf asdf asd f asd fa sdf asd fasdklfj;lkjasd f asdfjasdfkasdf");
            } else {
                auction.setDescription("some longer foo stuff");
            }
            dummyAuctions.add(auction);
        }

        return new AppResponse<>(true, dummyAuctions, "OK");
    }
}
