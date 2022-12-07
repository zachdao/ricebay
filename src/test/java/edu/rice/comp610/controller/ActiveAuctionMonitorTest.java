package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Bid;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import static edu.rice.comp610.testing.Dates.parseDate;
import static org.mockito.Mockito.*;

public class ActiveAuctionMonitorTest {

    private final AuctionManager auctionManager = mock(AuctionManager.class);
    private final BidManager bidManager = mock(BidManager.class);

    private static final Auction NEW_AUCTION = new Auction();
    static {
        NEW_AUCTION.setTitle("New Auction");
        NEW_AUCTION.setDescription("New Auction Description");
        NEW_AUCTION.setBidIncrement(1.0);
        NEW_AUCTION.setMinimumBid(123.0);
        NEW_AUCTION.setStartDate(parseDate("2022-10-20"));
        NEW_AUCTION.setEndDate(parseDate("2022-10-21"));
        NEW_AUCTION.setTaxPercent(0.06f);
        NEW_AUCTION.setPublished(true);
        NEW_AUCTION.setId(UUID.randomUUID());
        NEW_AUCTION.setOwnerId(UUID.randomUUID());
    }

    private static final Bid CURRENT_BID = new Bid();
    static {
        CURRENT_BID.setId(UUID.randomUUID());
        CURRENT_BID.setAmount(2.0);
        CURRENT_BID.setAuctionId(NEW_AUCTION.getId());
        CURRENT_BID.setOwnerId(UUID.randomUUID());
    }

    @Test
    void doExpiryCheckTest() throws DatabaseException, BadRequestException {
        when(auctionManager.expired()).thenReturn(List.of(NEW_AUCTION));
        when(bidManager.getCurrentBid(NEW_AUCTION.getId())).thenReturn(CURRENT_BID);
        new ActiveAuctionMonitor(auctionManager, bidManager).doExpiryCheck();
        assertFalse(NEW_AUCTION.getPublished());
        assertEquals(CURRENT_BID.getOwnerId(), NEW_AUCTION.getWinnerId());
        verify(auctionManager).save(eq(NEW_AUCTION));
        verify(bidManager).getCurrentBid(eq(NEW_AUCTION.getId()));
    }
}
