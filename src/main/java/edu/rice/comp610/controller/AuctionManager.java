package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.model.Picture;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface AuctionManager {
    UUID save(Auction auction) throws BadRequestException, DatabaseException;
    Auction get(UUID id) throws ObjectNotFoundException, DatabaseException;
    Auction get(UUID id, UUID viewerUD) throws ObjectNotFoundException, DatabaseException;
    List<Auction> getInIDs(List<UUID> auctionIds) throws DatabaseException;
    List<Auction> getInIDs(List<UUID> auctionIds, boolean includedUnpublished) throws DatabaseException;
    List<Auction> search(AuctionQuery query) throws DatabaseException;
    List<Auction> expired() throws DatabaseException;
    List<Auction> recentlyViewed(UUID viewerId) throws DatabaseException;
    List<Auction> purchases(UUID buyerId) throws DatabaseException;
    List<Auction> userAuctions(UUID ownerId) throws DatabaseException;
    List<Category> categories() throws DatabaseException;
    List<Category> categories(UUID auctionId) throws DatabaseException;
    List<Picture> addImages(List<byte[]> images, UUID auctionId) throws ObjectNotFoundException, DatabaseException;
    List<Picture> getImages(UUID auctionId) throws DatabaseException;
    void addCategories(List<String> categoryNames, UUID auctionId) throws ObjectNotFoundException, DatabaseException;
}
