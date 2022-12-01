package edu.rice.comp610.model;

import edu.rice.comp610.controller.AuctionQuery;
import edu.rice.comp610.model.*;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.ObjectNotFoundException;
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
    QueryManager queryManager = mock(QueryManager.class);
    StandardAuctionManager auctionManager = new StandardAuctionManager(queryManager, databaseManager);

    @BeforeEach
    void setUp() {
        when(queryManager.makeLoadQuery(any(), any()))
                .thenReturn(new Query<>());
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
    }

    @Test
    void createAndLoadAuction() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        UUID newUUID = auctionManager.save(NEW_AUCTION);
        assertNotNull(newUUID);
    }

    @Test
    @Disabled
    void createAuctionWithNonExistentCategory() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of()); // empty result

        assertThrows(BadRequestException.class, () -> auctionManager.save(NEW_AUCTION));
    }

    @Test
    void createAuctionWithNonExistentOwner() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of()); // empty result
        when(databaseManager.loadObjects(any(Query.class), eq(CAT_ELECTRONICS.getId())))
                .thenReturn(List.of(CAT_ELECTRONICS));

        assertThrows(BadRequestException.class, () -> auctionManager.save(NEW_AUCTION));
    }


    @Test
    void updateExistingAuction() throws Exception{
        NEW_AUCTION.setId(UUID.randomUUID());
        when(databaseManager.loadObjects(any(Query.class), eq(ACCOUNT_BOB.getId())))
                .thenReturn(List.of(ACCOUNT_BOB));
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of(NEW_AUCTION));

        UUID uuid = auctionManager.save(NEW_AUCTION);
        assertNotNull(uuid);

        assertEquals(NEW_AUCTION.getId(), uuid);
    }

    @Test
    void updateNonExistingAuction() throws Exception{
        NEW_AUCTION.setId(UUID.randomUUID());
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> auctionManager.save(NEW_AUCTION));
    }


    @Test
    void loadExistingAuction() throws Exception{
        NEW_AUCTION.setId(UUID.randomUUID());
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of(NEW_AUCTION));

        Auction auction = auctionManager.get(NEW_AUCTION.getId());
        assertEquals(NEW_AUCTION.getId(), auction.getId());
    }

    @Test
    void loadNonExistingAuction() throws Exception {
        NEW_AUCTION.setId(UUID.randomUUID());
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_AUCTION.getId())))
                .thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> auctionManager.get(NEW_AUCTION.getId()));
    }

    @Test
    void search() throws Exception {
        NEW_AUCTION.setId(UUID.randomUUID());
        when(databaseManager.loadObjects(any(Query.class), any()))
                .thenReturn(List.of(NEW_AUCTION));

        List<Auction> auctions = auctionManager.search(new AuctionQuery());
        assertEquals(1, auctions.size());
        assertEquals(NEW_AUCTION.getId(), auctions.get(0).getId());
    }
}