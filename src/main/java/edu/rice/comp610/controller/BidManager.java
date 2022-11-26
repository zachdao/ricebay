package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Bid;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.BadRequestException;

import java.util.UUID;

public interface BidManager {
    void placeBid(Auction auction, Bid bid) throws BadRequestException, DatabaseException;
    void updateBid(Auction auction, Bid bid) throws BadRequestException, DatabaseException, ObjectNotFoundException;
    Bid getCurrentBid(UUID auctionId) throws DatabaseException;
    Bid getUserBid(UUID auctionId, UUID ownerId) throws DatabaseException;
}
