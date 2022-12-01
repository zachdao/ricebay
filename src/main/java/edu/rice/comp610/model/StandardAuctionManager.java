package edu.rice.comp610.model;

import edu.rice.comp610.controller.AuctionManager;
import edu.rice.comp610.controller.AuctionQuery;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;

import java.util.*;

/**
 * Manages requests for creating, updating, searching and loading auctions.
 */
public class StandardAuctionManager implements AuctionManager {

    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public StandardAuctionManager(QueryManager queryManager, DatabaseManager databaseManager) {
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
    public UUID save(Auction auction) throws BadRequestException, DatabaseException {
        var accountQuery = queryManager.makeLoadQuery(Account.class, "id");
        List<Account> accounts = databaseManager.loadObjects(accountQuery, auction.getOwnerId());
        if (accounts.isEmpty()) {
            throw new BadRequestException("Invalid auction owner");
        }

        if (auction.getId() == null) {
            auction.setId(UUID.randomUUID());
            auction.setPublished(true);
        }
        var auctionQuery = queryManager.makeUpdateQuery(Auction.class);
        databaseManager.saveObjects(auctionQuery, auction);

        return auction.getId();
    }

    /**
     * Loads an existing auction and returns it. The id must be that of an existing auction.
     *
     * @param auctionId the id of auction to load
     * @return a response containing the auction; or an error message, if an error occurred (e.g., the auction could not
     * be found).
     */
    public Auction get(UUID auctionId) throws DatabaseException, ObjectNotFoundException {
        var loadQuery = queryManager.makeLoadQuery(Auction.class, "id");
        List<Auction> auctions = databaseManager.loadObjects(loadQuery, auctionId);

        if (auctions.size() != 1) {
            throw new ObjectNotFoundException("Could not find auction for id=" + auctionId.toString());
        }
        return auctions.get(0);
    }

    /**
     * Searches for auctions matching query string and returns matching results.
     *
     * @param query the {@link AuctionQuery} object to match against fields in the auction.
     * @return result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    public List<Auction> search(AuctionQuery query) throws DatabaseException {
        // find auction by category and text
        var auctionQuery = queryManager.makeLoadQuery(Auction.class, query.getQueryMap().getOrDefault("filterBy", new String[]{}));
        return databaseManager.loadObjects(auctionQuery, query.getQueryMap().getOrDefault("filterByValues", new String[]{}));
    }

    public List<Category> categories() throws DatabaseException {
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Category.class));
    }

    public List<Category> categories(UUID auctionId) throws DatabaseException {
        List<AuctionCategory> auctionCategories = databaseManager.loadObjects(queryManager.makeLoadQuery(AuctionCategory.class, "auction_id"), auctionId);

        // TODO: Remove the for loop once we get filters
        List<Category> categories = new ArrayList<>();
        for (AuctionCategory auctionCategory : auctionCategories) {
            categories.addAll(databaseManager.loadObjects(queryManager.makeLoadQuery(Category.class, "id"), auctionCategory.getCategoryId()));
        }
        return categories;
    }

    public List<Picture> addImages(List<String> images, UUID auctionId) throws ObjectNotFoundException, DatabaseException {
        return null;
    }

    /**
     * Associates a given auction to a list of categories to help buyers find the item.
     *
     * @param categoryNames list of category names.
     * @param auctionId     the auction_id the categories will be associated with
     */
    public void addCategories(List<String> categoryNames, UUID auctionId) throws DatabaseException {

        // Translate the category names into the category ids
        var getCategoryIdQuery = queryManager.makeLoadQuery(Category.class);

        // This is the right way, but not working right now
        // List<Category> categoryObjs = databaseManager.loadObjects(getCategoryIdQuery, categoryNames);
        List<Category> categoryObjs = databaseManager.loadObjects(getCategoryIdQuery);

        // TODO: Remove this once we get filters
        Set<String> auctionCategoryNames = new HashSet<>(categoryNames);

        // Add each combination of auctionId and categoryId to the auction_category table
        for (Category cat: categoryObjs) {

            if (auctionCategoryNames.contains(cat.getName())) {

                // Create new auctionCategory object
                AuctionCategory auctionCategory = new AuctionCategory();
                auctionCategory.setAuctionId(auctionId);
                auctionCategory.setCategoryId(cat.getId());

                var addCategoriesQuery = queryManager.makeUpdateQuery(AuctionCategory.class, false);
                databaseManager.saveObjects(addCategoriesQuery, auctionCategory);
            }
        }
    }
}
