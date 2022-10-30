package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.AuctionState;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.store.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static edu.rice.comp610.testing.Dates.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionManagerTest {

    private static final Category CAT_ELECTRONICS = new Category();
    private static final Account ACCOUNT_BOB = new Account();

    static {
        CAT_ELECTRONICS.setId(UUID.randomUUID());
        ACCOUNT_BOB.setId(UUID.randomUUID());
    }
    Auction NEW_AUCTION = new Auction();
    DatabaseManager databaseManager = mock(DatabaseManager.class);
    AuctionManager auctionManager = new AuctionManager(databaseManager);

    @BeforeEach
    void setUp() {
        NEW_AUCTION.setTitle("New Auction");
        NEW_AUCTION.setDescription("New Auction Description");
        NEW_AUCTION.setBidIncrement(1);
        NEW_AUCTION.setMinimumBid(123);
        NEW_AUCTION.setStartDate(parseDate("2022-10-20"));
        NEW_AUCTION.setEndDate(parseDate("2022-10-20"));
        NEW_AUCTION.setSalesTaxRate(0.06f);
        NEW_AUCTION.setState(AuctionState.ACTIVE);
        NEW_AUCTION.setCategoryIds(List.of(CAT_ELECTRONICS.getId()));
        NEW_AUCTION.setOwnerId(ACCOUNT_BOB.getId());
        NEW_AUCTION.setId(null);
    }

    @Test
    void createAndLoadAuction() {
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(eq(Category.class), anyString(), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void createAuctionWithNonExistentCategory() {
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(eq(Category.class), anyString(), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of()); // empty result

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Category ID " + CAT_ELECTRONICS.getId() + " does not exist", response.getMessage());
    }

    @Test
    void createAuctionWithNonExistentOwner() {
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of()); // empty result
        when(databaseManager.loadObjects(eq(Category.class), anyString(), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        AppResponse<UUID> response = auctionManager.createAuction(NEW_AUCTION);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("Account ID " + ACCOUNT_BOB.getId() + " does not exist", response.getMessage());
    }


    @Disabled
    @Test
    void updateAuction() {
        // TODO: Implement this test
        fail();
    }

    @Disabled
    @Test
    void loadAuction() {
        // TODO: Implement this test
        fail();
    }

    @Disabled
    @Test
    void search() {
        // TODO: Implement this test
        fail();
    }
}