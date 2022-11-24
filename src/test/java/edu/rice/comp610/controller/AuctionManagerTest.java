package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static edu.rice.comp610.testing.Dates.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionManagerTest {

    private static final Category CAT_ELECTRONICS = new Category();
    private static final Account ACCOUNT_BOB = new Account();

    static {
        CAT_ELECTRONICS.setId(123);
        ACCOUNT_BOB.setId(UUID.randomUUID());
    }
    Auction NEW_AUCTION = new Auction();
    DatabaseManager databaseManager = mock(DatabaseManager.class);
    QueryManager queryManager = new QueryManager();
    AuctionManager auctionManager = new AuctionManager(queryManager, databaseManager);

    @BeforeEach
    void setUp() {
        NEW_AUCTION.setTitle("New Auction");
        NEW_AUCTION.setDescription("New Auction Description");
        NEW_AUCTION.setBidIncrement(1.0);
        NEW_AUCTION.setMinimumBid(123.0);
        NEW_AUCTION.setStartDate(parseDate("2022-10-20"));
        NEW_AUCTION.setEndDate(parseDate("2022-10-20"));
        NEW_AUCTION.setTaxPercent(0.06f);
        NEW_AUCTION.setPublished(true);
        NEW_AUCTION.setCategoryIds(List.of(CAT_ELECTRONICS.getId()));
        NEW_AUCTION.setOwnerId(ACCOUNT_BOB.getId());
        NEW_AUCTION.setId(UUID.randomUUID());
    }

    @Test
    void createAndLoadAuction() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void createAuctionWithNonExistentCategory() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of()); // empty result

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Category ID " + CAT_ELECTRONICS.getId() + " does not exist", response.getMessage());
    }

    @Test
    void createAuctionWithNonExistentOwner() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of()); // empty result
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Account ID " + ACCOUNT_BOB.getId() + " does not exist", response.getMessage());
    }


    @Test
    void updateExistingAuction() throws Exception{
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of(NEW_AUCTION));

        AppResponse<UUID> response = auctionManager.updateAuction(NEW_AUCTION);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());

        assertEquals(NEW_AUCTION.getId(), response.getData());
    }

    @Test
    void updateNonExistingAuction() throws Exception{
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of());

        AppResponse<UUID> response = auctionManager.updateAuction(NEW_AUCTION);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Auction ID " + NEW_AUCTION.getId() + " does not exist", response.getMessage());
    }


    @Test
    void loadExistingAuction() throws Exception{

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of(NEW_AUCTION));

        AppResponse<UUID> response = auctionManager.loadAuction(NEW_AUCTION.getId());
        assertEquals(NEW_AUCTION.getId(), response.getData());
    }

    @Test
    void loadNonExistingAuction() throws Exception{

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of());

        AppResponse<UUID> response = auctionManager.loadAuction(NEW_AUCTION.getId());
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Auction ID " + NEW_AUCTION.getId() + " does not exist", response.getMessage());
    }

    @Disabled
    @Test
    void search() {
        fail();

    }

    /*throws
    } Exception{

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of());

        AppResponse<List<Auction>> response = auctionManager.search(NEW_AUCTION.getId());
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Auction ID " + NEW_AUCTION.getId() + " does not exist", response.getMessage());
    } */
}
