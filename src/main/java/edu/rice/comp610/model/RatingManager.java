package edu.rice.comp610.model;

import edu.rice.comp610.controller.AppResponse;
import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Rating;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;
import edu.rice.comp610.store.DatabaseException;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;


/**
 * Updates the sellers rating
 */
public class RatingManager {
    private final Rating rating;
    private final QueryManager queryManager;
    private final DatabaseManager databaseManager;

    public RatingManager(Rating rating, QueryManager queryManager, DatabaseManager databaseManager) {
        this.rating = rating;
        this.queryManager = queryManager;
        this.databaseManager = databaseManager;
    }

    /**
     * Update Seller's rating give by Buyer
     * @param rating Rating object created by Buyer
     * @return AppResponse of type UUID. UUID is rated id.
     */
    public AppResponse<UUID> updateUserRating(Rating rating) {
        try {
            Query<Rating> saveQuery = queryManager.makeUpdateQuery(Rating.class);
            databaseManager.saveObjects(saveQuery, rating);

            return new AppResponse<>(true, rating.getRaterId(), "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates average rating for given list of ratings
     * @param listSellerRatings which is List of Rating objects
     * @return averageRating which is type Double
     */
    public Double getAverageUserRating(List<Rating> listSellerRatings) {
        ListIterator<Rating> iterator = listSellerRatings.listIterator();
        Double sum = 0.;
        int count = 0;
        while (iterator.hasNext()) {
            sum = sum + iterator.next().getRating();
            count++;
        }
        Double averageRating = sum / count;
        return averageRating;
    }

    /**
     * Method looks for all ratings of seller in DB. Seller's id is retreived from Rating object
     * passed as an argument
     * @param rating which is of type Rating
     * @return AppResponse object of type Double. Data field of thsi object is average rating of seller.
     */
    public AppResponse<Double> retrieveUserRating(Rating rating) {
        try {
            Query<Rating> loadQuery = queryManager.makeLoadQuery(Rating.class, "seller_id");
            List<Rating> ratingList = databaseManager.loadObjects(loadQuery, rating.getSellerId());

            Double averageRating = getAverageUserRating(ratingList);
            return new AppResponse<>(true, averageRating, "OK");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
