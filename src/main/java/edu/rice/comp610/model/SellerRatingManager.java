package edu.rice.comp610.model;

import edu.rice.comp610.controller.RatingManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.BadRequestException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * Updates the sellers rating
 */
public class SellerRatingManager implements RatingManager {
    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public SellerRatingManager(QueryManager queryManager, DatabaseManager databaseManager) {
        this.queryManager = queryManager;
        this.databaseManager = databaseManager;
    }

    /**
     * Update Seller's rating give by Buyer
     * @param rating Rating object created by Buyer
     */
    public void updateRating(Rating rating) throws DatabaseException, BadRequestException {
        if (rating.getRating() > 5 || rating.getRating() < 1) {
            throw new BadRequestException("Invalid rating", Collections.singletonMap("rating", "Rating must be between 1 and 5"));
        }
        Query<Rating> saveQuery = queryManager.makeUpdateQuery(Rating.class);
        databaseManager.saveObjects(saveQuery, rating);
    }

    /**
     * Calculates average rating for given list of ratings
     * @param userId the id of the user to get the rating for
     * @return the user's average rating
     */
    public Double getRating(UUID userId) throws DatabaseException {
        List<Rating> ratingsForUser = this.databaseManager.loadObjects(this.queryManager.makeLoadQuery(Rating.class, "seller_id"), userId);
        if (ratingsForUser.isEmpty()) {
            return 0.0;
        }
        var sum = ratingsForUser.stream().mapToInt(Rating::getRating).sum();
        double total = ratingsForUser.size() * 1.0;
        return sum / total;
    }
}
