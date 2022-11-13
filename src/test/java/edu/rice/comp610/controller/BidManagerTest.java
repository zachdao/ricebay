package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Bid;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static edu.rice.comp610.testing.Dates.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BidManagerTest {

    Bid NEW_BID = new Bid();
    {
        NEW_BID.setId(UUID.randomUUID());
        NEW_BID.setTimestamp(parseDate("2022-11-09"));
        NEW_BID.setAmount(150.0);
        NEW_BID.setAuctionId(UUID.randomUUID());
    }
    Bid OLD_BID = new Bid();
    {
        OLD_BID.setId(UUID.randomUUID());
        OLD_BID.setTimestamp(parseDate("2022-11-09"));
        OLD_BID.setAmount(100.0);
        OLD_BID.setAuctionId(UUID.randomUUID());
    }
    Bid LATEST_BID = new  Bid();{
        LATEST_BID.setId(UUID.randomUUID());
        LATEST_BID.setTimestamp(parseDate("2022-11-10"));
        LATEST_BID.setAmount(50.0);
        LATEST_BID.setOwnerId(UUID.randomUUID());
        LATEST_BID.setAuctionId(UUID.randomUUID());
    }

    Auction NEW_AUCTION = new Auction();
    {
        NEW_AUCTION.setTitle("Auction1");
        NEW_AUCTION.setDescription("Auction1 Description");
        NEW_AUCTION.setBidIncrement(7);
        NEW_AUCTION.setMinimumBid(123);
        NEW_AUCTION.setStartDate(parseDate("2022-11-25"));
        NEW_AUCTION.setEndDate(parseDate("2022-11-11"));
        NEW_AUCTION.setTaxPercent(0.06f);
        NEW_AUCTION.setPublished(true);
        NEW_AUCTION.setId(UUID.randomUUID());
    }



    QueryManager queryManager = new QueryManager();
    DatabaseManager databaseManager = mock(DatabaseManager.class);

    Bid bid = mock(Bid.class);
    BidManager bidManager = new BidManager(bid, queryManager, databaseManager);

    @Test
    void findMaxBidTest(){
        List<Bid> listOfBids = new ArrayList<>();
        listOfBids.add(0, OLD_BID);
        listOfBids.add(1, NEW_BID);


        Double maxBid = bidManager.findMaxBid(listOfBids);
        assertEquals(150, maxBid);
    }

    @Test
    void findCurrentBidTest1() throws Exception {

        BidManager bidManagerTest = new BidManager(NEW_BID, queryManager,databaseManager);

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of(NEW_BID, OLD_BID));

        AppResponse<Double> response = bidManagerTest.findCurrentBid(NEW_BID);
        assertEquals(150.0, response.getData());
    }

    @Test
    void findCurrentBidTest2() throws  Exception{

        BidManager bidManagerTest = new BidManager(LATEST_BID, queryManager, databaseManager);

        when(databaseManager.loadObjects(any(Query.class), eq(LATEST_BID.getAuctionId())))
                .thenReturn(List.of(NEW_BID, OLD_BID, LATEST_BID));

        AppResponse<Double> response = bidManagerTest.findCurrentBid(LATEST_BID);
        assertEquals(150, response.getData());
    }

    @Test
    void findBidIncrementTest() throws Exception{

        BidManager bidManagerTest = new BidManager(NEW_BID, queryManager, databaseManager);

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of(NEW_AUCTION));

        AppResponse<Double> response = bidManagerTest.findBidIncrement(NEW_BID);
        assertEquals(7, response.getData());
    }

    @Test
    void getAuctionOwnerByBidTest() throws Exception{

        Account NEW_ACCOUNT = new Account();
        {
            NEW_ACCOUNT.setId(UUID.randomUUID());
        }

        Auction NEW_AUCTION = new Auction();
        {
            NEW_AUCTION.setTitle("Auction1");
            NEW_AUCTION.setDescription("Auction1 Description");
            NEW_AUCTION.setBidIncrement(7);
            NEW_AUCTION.setMinimumBid(123);
            NEW_AUCTION.setStartDate(parseDate("2022-11-25"));
            NEW_AUCTION.setEndDate(parseDate("2022-11-11"));
            NEW_AUCTION.setTaxPercent(0.06f);
            NEW_AUCTION.setPublished(true);
            NEW_AUCTION.setId(UUID.randomUUID());
            NEW_AUCTION.setOwnerId(NEW_ACCOUNT.getId());
        }

        BidManager bidManagerTest = new BidManager(NEW_BID, queryManager, databaseManager);

        when(databaseManager.loadObjects(any(Query.class), eq(NEW_BID.getAuctionId())))
                .thenReturn(List.of(NEW_AUCTION));

        AppResponse<UUID> response = bidManagerTest.getAuctionOwnerByBid(NEW_BID);
        assertEquals(NEW_ACCOUNT.getId(), response.getData());
    }

    @Test
    void checkConformityOfBid() throws Exception{
        //This test needs to wait for databaseManager.LoadObject() method
    }

}
