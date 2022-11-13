package edu.rice.comp610.controller;


import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Rating;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


public class RatingManagerTest {
    Rating rating = mock(Rating.class);

    Account account = mock(Account.class);
    DatabaseManager databaseManager = mock(DatabaseManager.class);
    private final QueryManager queryManager = new QueryManager();
    RatingManager ratingManager = new RatingManager(rating, queryManager, databaseManager);

    private static final Rating RATING1 = new Rating();
    {
        RATING1.setRating(5);
        RATING1.setRaterId(UUID.randomUUID());
        RATING1 .setSellerId(UUID.randomUUID());
    }

    private static final Rating RATING2 = new Rating();
    {
        RATING2.setRating(4);
        RATING2.setRaterId(UUID.randomUUID());
        RATING2 .setSellerId(UUID.randomUUID());
    }


    @Test
    void getAverageUserRatingTest(){
        List<Rating> ratingList = new ArrayList<>();
        ratingList.add(0, RATING1);
        ratingList.add(1,RATING2);

        Double result = ratingManager.getAverageUserRating(ratingList);

        assertEquals(4.5, result);
    }

    @Test
    void retrieveUserRatingTest() throws Exception{
        RatingManager RatingManagerTest = new RatingManager(RATING1, queryManager, databaseManager);

        when(databaseManager.loadObjects(any(Query.class), eq(RATING1.getSellerId())))
                .thenReturn(List.of(RATING1));

        AppResponse<Double> response = RatingManagerTest.retrieveUserRating(RATING1);
        assertEquals(5.0, response.getData());
    }

    @Test
    void updateUserRatingTest() {
        // Needs to upgrade query manager for Rating table since PK is (buyer_id, seller_id)
    }


}
