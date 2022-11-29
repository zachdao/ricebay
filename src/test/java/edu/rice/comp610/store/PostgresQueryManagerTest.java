package edu.rice.comp610.store;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Rating;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PostgresQueryManagerTest {

    private final PostgresQueryManager queryManager = new PostgresQueryManager();

    @Test
    void makeColumnsToAccessorsMap() {
        var map = PostgresQueryManager.makeColumnsToAccessorsMap(Auction.class);
        assertNotNull(map);
        assertEquals(map.keySet(), Set.of(
                "bid_increment", "category_ids", "description", "end_date", "id", "minimum_bid", "owner_id",
                "published", "start_date", "tax_percent", "title"));
        for (var entry : map.entrySet()) {
            assertNotNull(entry.getValue().getter, entry.getKey() + " has null getter");
            assertNotNull(entry.getValue().setter, entry.getKey() + " has null setter");
        }
    }
    @Test
    void makeLoadQuery() {
        Query<Auction> query = queryManager.makeLoadQuery(Auction.class, "id");
        assertEquals("SELECT bid_increment, description, end_date, id, minimum_bid, owner_id, published, " +
                "start_date, tax_percent, title FROM auction " +
                "WHERE id = ?",
                query.getSql());
    }

    @Test
    void makeUpdateQuery() {
        Query<Auction> query = queryManager.makeUpdateQuery(Auction.class);
        assertEquals("INSERT INTO auction (bid_increment, description, end_date, id, " +
                "minimum_bid, owner_id, published, start_date, tax_percent, title) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET bid_increment = ?, description = ?, end_date = ?, " +
                "minimum_bid = ?, owner_id = ?, published = ?, start_date = ?, tax_percent = ?, title = ?",
                query.getSql());
    }

    @Test
    void makeUpdateQueryCompoundPK() {
        Query<Rating> query = queryManager.makeUpdateQuery(Rating.class);
        assertEquals("INSERT INTO rating (rater_id, rating, seller_id) " +
                        "VALUES (?, ?, ?) " +
                        "ON CONFLICT (rater_id, seller_id) DO UPDATE SET rating = ?",
                query.getSql());
    }
}