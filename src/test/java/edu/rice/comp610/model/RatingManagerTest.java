package edu.rice.comp610.model;


import edu.rice.comp610.model.*;
import edu.rice.comp610.store.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


public class RatingManagerTest {

    DatabaseManager databaseManager = mock(DatabaseManager.class);
    QueryManager queryManager = mock(QueryManager.class);
    Filters filters = mock(Filters.class);
    SellerRatingManager ratingManager = new SellerRatingManager(queryManager, databaseManager);

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

    @BeforeEach
    void setUp() {
        when(queryManager.makeLoadQuery(any(), any()))
                .thenReturn(new Query<>());
        when(queryManager.filters()).thenReturn(filters);
    }

    @Test
    void getRatingTest() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(RATING1.getSellerId())))
                .thenReturn(List.of(RATING1, RATING2));
        Double result = ratingManager.getRating(RATING1.getSellerId());

        assertEquals(4.5, result);
    }

    @Disabled
    @Test
    void updateUserRatingTest() {
        // Needs to upgrade query manager for Rating table since PK is (buyer_id, seller_id)
    }


}
