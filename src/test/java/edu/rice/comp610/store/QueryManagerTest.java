package edu.rice.comp610.store;

import edu.rice.comp610.model.Auction;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QueryManagerTest {

    private final QueryManager queryManager = new QueryManager();

    @Test
    void makeColumnsToAccessorsMap() {
        var map = QueryManager.makeColumnsToAccessorsMap(Auction.class);
        assertNotNull(map);
        assertEquals(map.keySet(), Set.of(
                "bid_increment", "category_ids", "description", "end_date", "id", "minimum_bid", "owner_id", "sales_tax_rate",
                "start_date", "state", "title"));
        for (var entry : map.entrySet()) {
            assertNotNull(entry.getValue().getter, entry.getKey() + " has null getter");
            assertNotNull(entry.getValue().setter, entry.getKey() + " has null setter");
        }
    }
    @Test
    void makeLoadQuery() {
        String sql = queryManager.makeLoadQuery(Auction.class, "id");
        assertEquals("SELECT bid_increment, description, end_date, id, minimum_bid, owner_id, " +
                "sales_tax_rate, start_date, state, title FROM auction " +
                "WHERE id = ?", sql);
    }

    @Test
    void makeUpdateQuery() {
        String sql = queryManager.makeUpdateQuery(Auction.class);
        assertEquals("INSERT INTO auction (bid_increment, description, end_date, id, " +
                "minimum_bid, owner_id, sales_tax_rate, start_date, state, title) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) UPDATE bid_increment = ?, description = ?, end_date = ?, " +
                "minimum_bid = ?, owner_id = ?, sales_tax_rate = ?, start_date = ?, state = ?, title = ?", sql);
    }
}