package edu.rice.comp610.model;

import edu.rice.comp610.controller.AuctionManager;
import edu.rice.comp610.controller.AuctionQuery;
import edu.rice.comp610.store.AuctionSortField;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import org.eclipse.jetty.util.ArrayUtil;

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
        var accountQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("id"));
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
        return this.get(auctionId, null);
    }

    /**
     * Loads an existing auction and returns it. The id must be that of an existing auction.
     *
     * @param auctionId the id of auction to load
     * @param viewerId if not null then account id a user viewing the auction
     * @return a response containing the auction; or an error message, if an error occurred (e.g., the auction could not
     * be found).
     */
    public Auction get(UUID auctionId, UUID viewerId) throws DatabaseException, ObjectNotFoundException {
        var loadQuery = queryManager.makeLoadQuery(Auction.class, queryManager.filters().makeEqualityFilter("id"));
        List<Auction> auctions = databaseManager.loadObjects(loadQuery, auctionId);

        if (auctions.size() != 1) {
            throw new ObjectNotFoundException("Could not find auction for id=" + auctionId.toString());
        }

        // Record auction view in the database
        // Only if the viewer is not the auction owner
        if (viewerId != null && !viewerId.equals(auctions.get(0).getOwnerId())) {
            AuctionView auctionView = new AuctionView();
            auctionView.setAuctionId(auctionId);
            auctionView.setViewerId(viewerId);
            auctionView.setTimestamp(new Date());

            var insertAuctionViewQuery = queryManager.makeUpdateQuery(AuctionView.class);
            databaseManager.saveObjects(insertAuctionViewQuery, auctionView);
        }
        return auctions.get(0);
    }
    public List<Auction> getInIDs(List<UUID> auctionIds) throws DatabaseException {
        return this.getInIDs(auctionIds, false);
    }

    public List<Auction> getInIDs(List<UUID> auctionIds, boolean includeUnpublished) throws DatabaseException {
        if (auctionIds.isEmpty()) {
            return List.of();
        }
        var query = queryManager.makeLoadQuery(Auction.class,
                queryManager.filters().makeAndFilter(
                        queryManager.filters().makeEqualityFilter("published"),
                        queryManager.filters().makeInFilter("id", auctionIds.size())));
        return databaseManager.loadObjects(query, ArrayUtil.prependToArray(!includeUnpublished, auctionIds.toArray(), Object.class));
    }

    /**
     * Searches for auctions matching query string and returns matching results.
     *
     * @param query the {@link AuctionQuery} object to match against fields in the auction.
     * @return result of the search, which contains a list of auction objects if successful, or an error message
     * otherwise.
     */
    public List<Auction> search(AuctionQuery query) throws DatabaseException {
        // ADDING SORT, LETS JUST ASSUME SORT BY TITLE UNLESS SPECIFIED
        // find auction by category and text
        // No text filters and no category filters
        //if (query.getFilters().length == 0 && query.hasCategories().isEmpty()) {
        if (query.getFilters().length == 0 && query.hasCategories().isEmpty()) {
            var auctionQuery = queryManager.makeLoadQuery(Auction.class,
                    queryManager.filters().makeEqualityFilter("published"),
                    query.getSortField(),
                    query.isSortAscending());
            return databaseManager.loadObjects(auctionQuery, true);
        // the search has no category, but does have text to filter by
        } else if (query.hasCategories().isEmpty()) {
            var auctionQuery = queryManager.makeLoadQuery(Auction.class,
                    queryManager.filters().makeAndFilter(
                            queryManager.filters().makeEqualityFilter("published"),
                            queryManager.filters().makeOrFilter(query.getFilters())),
                    query.getSortField(),
                    query.isSortAscending());
            return databaseManager.loadObjects(auctionQuery, ArrayUtil.prependToArray(true, query.getValues(), Object.class));
        // the search has no text based filtering but does have category(s) to filter by
        } else if (query.getFilters().length == 0) {
            Object[] auctionIds = getAuctionIdsFromCategories(query);
            if (auctionIds.length == 0) {
                return List.of();
            }
            Object[] objects = ArrayUtil.prependToArray(true, auctionIds, Object.class);
            return databaseManager.loadObjects(queryManager.makeLoadQuery(Auction.class,
                            queryManager.filters().makeAndFilter(
                                    queryManager.filters().makeEqualityFilter("published"),
                                    queryManager.filters().makeInFilter("id", auctionIds.length)),
                            query.getSortField(),
                            query.isSortAscending()),
                    objects);
        // the search has both text and category filters
        } else {
            Object[] auctionIds = getAuctionIdsFromCategories(query);
            Object[] objects = ArrayUtil.add(ArrayUtil.prependToArray(true, auctionIds, Object.class), query.getValues());
            if (auctionIds.length == 0) {
                return List.of();
            }
            return databaseManager.loadObjects(queryManager.makeLoadQuery(Auction.class,
                        queryManager.filters().makeAndFilter(
                                queryManager.filters().makeEqualityFilter("published"),
                                queryManager.filters().makeInFilter("id", auctionIds.length),
                                queryManager.filters().makeOrFilter(query.getFilters())),
                            query.getSortField(),
                            query.isSortAscending()),
                    objects);
        }
    }

    private Object[] getAuctionIdsFromCategories(AuctionQuery query) throws DatabaseException {
        var categoryQuery = queryManager.makeLoadQuery(Category.class, queryManager.filters().makeInFilter("name", query.hasCategories().size()));
        var categoryIds = databaseManager.loadObjects(categoryQuery, query.hasCategories().toArray()).stream().map(Category::getId).toArray();
        var auctionCategoryQuery = queryManager.makeLoadQuery(AuctionCategory.class, queryManager.filters().makeInFilter("category_id", categoryIds.length));
        return databaseManager.loadObjects(auctionCategoryQuery, categoryIds).stream().map(AuctionCategory::getAuctionId).toArray();
    }

    public List<Auction> expired() throws DatabaseException {
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Auction.class,
                queryManager.filters().makeAndFilter(
                        queryManager.filters().makeEqualityFilter("published"),
                        queryManager.filters().makeLessThanFilter("end_date"))), true, new Date());
    }

    public List<Auction> recentlyViewed(UUID viewerId) throws DatabaseException {
        var auctionViewQuery = queryManager.makeLoadQuery(AuctionView.class,
                queryManager.filters().makeEqualityFilter("viewer_id"));
        var auctionIds = databaseManager.loadObjects(auctionViewQuery, viewerId).stream().map(AuctionView::getAuctionId).toArray();
        if (auctionIds.length == 0) {
            return List.of();
        }
        var auctionQuery = queryManager.makeLoadQuery(Auction.class,
                queryManager.filters().makeAndFilter(
                        queryManager.filters().makeEqualityFilter("published"),
                        queryManager.filters().makeInFilter("id", auctionIds.length)
                ));
        return databaseManager.loadObjects(auctionQuery, ArrayUtil.prependToArray(true, auctionIds, Object.class));
    }

    public List<Auction> purchases(UUID buyerId) throws DatabaseException {
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Auction.class, queryManager.filters().makeEqualityFilter("winner_id"), AuctionSortField.END_DATE, false), buyerId);
    }

    public List<Auction> userAuctions(UUID ownerId) throws DatabaseException {
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Auction.class, queryManager.filters().makeEqualityFilter("owner_id"), AuctionSortField.END_DATE, false), ownerId);
    }

    public List<Category> categories() throws DatabaseException {
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Category.class));
    }

    public List<Category> categories(UUID auctionId) throws DatabaseException {
        List<AuctionCategory> auctionCategories = databaseManager.loadObjects(queryManager.makeLoadQuery(AuctionCategory.class, queryManager.filters().makeEqualityFilter("auction_id")), auctionId);

        var categoryIds = auctionCategories.stream().map(AuctionCategory::getCategoryId).toArray();
        if (categoryIds.length == 0) {
            System.err.format("Auction %s had no categories!", auctionId.toString());
            return List.of();
        }
        return databaseManager.loadObjects(queryManager.makeLoadQuery(Category.class,
                queryManager.filters().makeInFilter("id", auctionCategories.size())),
                categoryIds);
    }

    public List<Picture> addImages(List<byte[]> images, UUID auctionId) throws DatabaseException {

        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            // Create new picture object
            Picture img = new Picture();
            img.setAuctionId(auctionId);
            img.setPictureData(images.get(i));
            img.setPictureSequence(i);
            img.setPictureName(String.format("Image %d", i));

            pictures.add(img);

            var addImagesQuery = queryManager.makeUpdateQuery(Picture.class, false);
            databaseManager.saveObjects(addImagesQuery, img);
        }
        return pictures;
    }

    public List<Picture> getImages(UUID auctionId) throws DatabaseException {
        // Grab the image data from the database
        var getImageQuery = queryManager.makeLoadQuery(Picture.class, queryManager.filters().makeEqualityFilter("auction_id"));
        return databaseManager.loadObjects(getImageQuery, auctionId);
    }

    /**
     * Associates a given auction to a list of categories to help buyers find the item.
     *
     * @param categoryNames list of category names.
     * @param auctionId     the auction_id the categories will be associated with
     */
    public void addCategories(List<String> categoryNames, UUID auctionId) throws DatabaseException {

        // Translate the category names into the category ids
        var getCategoryIdQuery = queryManager.makeLoadQuery(Category.class, queryManager.filters().makeInFilter("name", categoryNames.size()));
        List<Category> categoryObjs = databaseManager.loadObjects(getCategoryIdQuery, categoryNames.toArray());

        // Add each combination of auctionId and categoryId to the auction_category table
        for (Category cat: categoryObjs) {
            // Create new auctionCategory object
            AuctionCategory auctionCategory = new AuctionCategory();
            auctionCategory.setAuctionId(auctionId);
            auctionCategory.setCategoryId(cat.getId());

            var addCategoriesQuery = queryManager.makeUpdateQuery(AuctionCategory.class, false);
            databaseManager.saveObjects(addCategoriesQuery, auctionCategory);
        }
    }
}
