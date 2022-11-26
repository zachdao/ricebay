package edu.rice.comp610.model;

import edu.rice.comp610.model.*;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import static edu.rice.comp610.testing.Dates.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BidManagerTest {
    Account ACCOUNT_1 = new Account();
    {
        ACCOUNT_1.setId(UUID.randomUUID());
    }

    Account ACCOUNT_2 = new Account();
    {
        ACCOUNT_2.setId(UUID.randomUUID());
    }

    Account ACCOUNT_3 = new Account();
    {
        ACCOUNT_3.setId(UUID.randomUUID());
    }

    Auction AUCTION = new Auction();
    {
        AUCTION.setTitle("Auction1");
        AUCTION.setDescription("Auction1 Description");
        AUCTION.setBidIncrement(7);
        AUCTION.setMinimumBid(123);
        AUCTION.setStartDate(parseDate("2022-11-25"));
        AUCTION.setEndDate(parseDate("2022-11-11"));
        AUCTION.setTaxPercent(0.06f);
        AUCTION.setPublished(true);
        AUCTION.setId(UUID.randomUUID());
        AUCTION.setOwnerId(UUID.randomUUID());
    }
    Bid NEW_BID = new Bid();
    {
        NEW_BID.setId(UUID.randomUUID());
        NEW_BID.setTimestamp(parseDate("2022-11-09"));
        NEW_BID.setAmount(150.0);
        NEW_BID.setAuctionId(AUCTION.getId());
        NEW_BID.setOwnerId(ACCOUNT_1.getId());
    }
    Bid OLD_BID = new Bid();
    {
        OLD_BID.setId(UUID.randomUUID());
        OLD_BID.setTimestamp(parseDate("2022-11-09"));
        OLD_BID.setAmount(100.0);
        OLD_BID.setAuctionId(AUCTION.getId());
        OLD_BID.setOwnerId(ACCOUNT_2.getId());
    }
    Bid LATEST_BID = new  Bid();{
        LATEST_BID.setId(UUID.randomUUID());
        LATEST_BID.setTimestamp(parseDate("2022-11-10"));
        LATEST_BID.setAmount(250.0);
        LATEST_BID.setAuctionId(AUCTION.getId());
        LATEST_BID.setOwnerId(ACCOUNT_3.getId());
    }

    QueryManager queryManager = mock(QueryManager.class);
    DatabaseManager databaseManager = mock(DatabaseManager.class);
    StandardBidManager bidManager = new StandardBidManager(queryManager, databaseManager);

    @BeforeEach
    void setUp() {
        when(queryManager.makeLoadQuery(any(), any()))
                .thenReturn(new Query<>());
    }

    @Test
    void getCurrentBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(AUCTION.getId())))
                .thenReturn(List.of(NEW_BID, OLD_BID));

        Bid bid = bidManager.getCurrentBid(AUCTION.getId());
        assertEquals(NEW_BID.getAmount(), bid.getAmount());
    }

    @Test
    void getCurrentBid2() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(LATEST_BID.getAuctionId())))
                .thenReturn(List.of(NEW_BID, OLD_BID, LATEST_BID));

        Bid bid = bidManager.getCurrentBid(LATEST_BID.getAuctionId());
        assertEquals(LATEST_BID.getAmount(), bid.getAmount());
    }

    @Test
    void placeBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> bidManager.placeBid(AUCTION, NEW_BID));
    }

    @Test
    void placeBid2() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of(OLD_BID));

        assertDoesNotThrow(() -> bidManager.placeBid(AUCTION, NEW_BID));
    }

    @Test
    void placeBidInvalidBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of(LATEST_BID));

        assertThrows(BadRequestException.class, () -> bidManager.placeBid(AUCTION, NEW_BID));
    }

    @Test
    void updateBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), any()))
                .thenReturn(List.of(NEW_BID));
        var bid = new Bid();
        bid.setAmount(20000.0);
        bid.setOwnerId(NEW_BID.getOwnerId());
        assertDoesNotThrow(() -> bidManager.updateBid(AUCTION, bid));
    }

    @Test
    void updateMissingBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of());
        assertThrows(ObjectNotFoundException.class, () -> bidManager.updateBid(AUCTION, NEW_BID));
    }

    @Test
    void getUserBid() throws Exception {
        when(databaseManager.loadObjects(any(Query.class), any()))
                .thenReturn(List.of(NEW_BID));

        Bid bid = bidManager.getUserBid(NEW_BID.getAuctionId(), NEW_BID.getOwnerId());
        assertEquals(NEW_BID.getId(), bid.getId());
    }

}
