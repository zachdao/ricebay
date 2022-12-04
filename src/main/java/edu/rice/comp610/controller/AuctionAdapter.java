package edu.rice.comp610.controller;

import edu.rice.comp610.model.*;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.BadRequestException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuctionAdapter {
    private final AccountManager accountManager;
    private final AuctionManager auctionManager;
    private final RatingManager ratingManager;
    private final BidManager bidManager;

    public AuctionAdapter(AccountManager accountManager, AuctionManager auctionManager, RatingManager ratingManager, BidManager bidManager) {
        this.accountManager = accountManager;
        this.auctionManager = auctionManager;
        this.ratingManager = ratingManager;
        this.bidManager = bidManager;
    }

    AppResponse<?> create(ViewAuction viewAuction, UUID ownerId) {
        try {
            Auction auction = new Auction();
            auction.setId(viewAuction.getId());
            auction.setTitle(viewAuction.getTitle());
            auction.setOwnerId(ownerId);
            auction.setDescription(viewAuction.getDescription());
            auction.setMinimumBid(viewAuction.getMinimumBid());
            auction.setBidIncrement(viewAuction.getBidIncrement());
            auction.setStartDate(viewAuction.getStartDate());
            auction.setEndDate(viewAuction.getEndDate());
            auction.setTaxPercent(viewAuction.getTaxPercent());
            UUID newId = this.auctionManager.save(auction);
            if (viewAuction.getImages() != null) {
                this.auctionManager.addImages(viewAuction.getImages().stream().map(String::getBytes).collect(Collectors.toList()), newId);
            }
            this.auctionManager.addCategories(viewAuction.getCategories() != null ? viewAuction.getCategories() : List.of(), newId);
            return new AppResponse<>(201, true, newId, "OK");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        }
    }

    AppResponse<?> update(ViewAuction viewAuction, UUID ownerId) {
        try {
            Auction auction = this.auctionManager.get(viewAuction.getId());
            if (!auction.getOwnerId().equals(ownerId)) {
                return new AppResponse<>(401, false, null, "Unauthorized");
            }
            if (!viewAuction.getTitle().isEmpty()) {
                auction.setTitle(viewAuction.getTitle());
            }
            if (!viewAuction.getDescription().isEmpty()) {
                auction.setDescription(viewAuction.getDescription());
            }
            if (viewAuction.getMinimumBid() != 0) {
                auction.setMinimumBid(viewAuction.getMinimumBid());
            }
            if (viewAuction.getBidIncrement() != 0) {
                auction.setBidIncrement(viewAuction.getBidIncrement());
            }
            if (viewAuction.getStartDate() != null) {
                auction.setStartDate(viewAuction.getStartDate());
            }
            if (viewAuction.getEndDate() != null) {
                auction.setEndDate(viewAuction.getEndDate());
            }
            if (viewAuction.getTaxPercent() != 0 ) {
                auction.setTaxPercent(viewAuction.getTaxPercent());
            }
            if (viewAuction.getImages() != null && !viewAuction.getImages().isEmpty()) {
                this.auctionManager.addImages(viewAuction.getImages().stream().map(String::getBytes).collect(Collectors.toList()), viewAuction.getId());
            }
            auction.setPublished(viewAuction.isPublished());
            // TODO: Diff categories and add new ones and delete removed ones
            this.auctionManager.save(auction);
            return new AppResponse<>(200, true, null, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        }
    }

    AppResponse<?> search(AuctionQuery query) {
        try {
            var auctions = this.auctionManager.search(query).stream()
                    .map(this::auctionToViewAuctionMapper)
                    .collect(Collectors.toList());
            return new AppResponse<>(200, true, auctions, "OK");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

    ViewAuction auctionToViewAuctionMapper(Auction auction) {
        ViewAuction viewAuction = new ViewAuction();
        viewAuction.setId(auction.getId());
        viewAuction.setTitle(auction.getTitle());
        viewAuction.setDescription(auction.getDescription());
        viewAuction.setMinimumBid(auction.getMinimumBid());
        viewAuction.setStartDate(auction.getStartDate());
        viewAuction.setEndDate(auction.getEndDate());
        try {
            Bid currentBid = this.bidManager.getCurrentBid(auction.getId());
            if (currentBid != null) {
                viewAuction.setCurrentBid(currentBid.getAmount());
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            System.err.format("Encountered DB error while loading current bid for auction %s%n", auction.getId().toString());
        }
        try {
            viewAuction.setImages(this.auctionManager.getImages(viewAuction.getId()).stream().map(Picture::getPictureData).map(String::new).collect(Collectors.toList()));
        } catch (DatabaseException e) {
            e.printStackTrace();
            System.err.format("Encountered DB error while loading images for auction %s%n", auction.getId().toString());
        }
        return viewAuction;
    }

    AppResponse<?> recentlyViewed(UUID viewerId) {
        try {
            var auctions = this.auctionManager.recentlyViewed(viewerId).stream()
                    .map(this::auctionToViewAuctionMapper)
                    .collect(Collectors.toList());
            return new AppResponse<>(200, true, auctions, "OK");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

    AppResponse<?> get(UUID id, ViewAccount user) {
        try {
            Auction auction = this.auctionManager.get(id, user.getId());
            ViewAuction viewAuction = new ViewAuction();
            viewAuction.setId(auction.getId());
            viewAuction.setTitle(auction.getTitle());
            viewAuction.setDescription(auction.getDescription());
            viewAuction.setMinimumBid(auction.getMinimumBid());
            viewAuction.setBidIncrement(auction.getBidIncrement());
            viewAuction.setStartDate(auction.getStartDate());
            viewAuction.setEndDate(auction.getEndDate());
            viewAuction.setPublished(auction.getPublished());
            viewAuction.setTaxPercent(auction.getTaxPercent());

            // Set categories
            List<Category> categories = this.auctionManager.categories(id);
            viewAuction.setCategories(categories.stream().map(Category::getName).collect(Collectors.toList()));

            Account owner = this.accountManager.get(auction.getOwnerId());
            ViewSeller seller = new ViewSeller();
            seller.setAlias(owner.getAlias());
            seller.setRating(this.ratingManager.getRating(auction.getOwnerId()));
            viewAuction.setSeller(seller);

            Bid currentBid = this.bidManager.getCurrentBid(id);
            if (currentBid != null) {
                viewAuction.setCurrentBid(currentBid.getAmount());
            }

            Bid bid = this.bidManager.getUserBid(id, user.getId());
            if (bid != null) {
                ViewBid userBid = new ViewBid();
                userBid.setAlias(user.getAlias());
                userBid.setBid(bid.getAmount());
                userBid.setMaxBid(bid.getMaxBid());
                userBid.setTimestamp(bid.getTimestamp());
                viewAuction.setUserBid(userBid);
            }
            List<Picture> pictures = this.auctionManager.getImages(id);
            viewAuction.setImages(pictures.stream()
                    .map(Picture::getPictureData)
                    .map(String::new)
                    .collect(Collectors.toList()));

            List<ViewBid> bids = new ArrayList<>();
            if (owner.getId().equals(user.getId())) {
                List<Bid> auctionBids = this.bidManager.getAuctionBids(id);
                for (Bid auctionBid : auctionBids) {
                    ViewBid viewBid = new ViewBid();
                    viewBid.setBid(auctionBid.getAmount());
                    viewBid.setTimestamp(auctionBid.getTimestamp());
                    Account bidder = this.accountManager.get(auctionBid.getOwnerId());
                    viewBid.setAlias(bidder.getAlias());
                    bids.add(viewBid);
                }
            }
            viewAuction.setBids(bids);

            return new AppResponse<>(200, true, viewAuction, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

    AppResponse<?> rateSeller(UUID raterId, UUID auctionId, int rating) {
        try {
            var auction = this.auctionManager.get(auctionId);
            var sellerRating = new Rating();
            sellerRating.setRating(rating);
            sellerRating.setSellerId(auction.getOwnerId());
            sellerRating.setRaterId(raterId);
            this.ratingManager.updateRating(sellerRating);
            return new AppResponse<>(200, true, null, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        }
    }


    AppResponse<?> placeBid(UUID bidderId, UUID auctionId, double bid, double maxBid) {
        try {
            // Check that the bidder exists
            this.accountManager.get(bidderId);
            // Check that the auction exists
            var auction = this.auctionManager.get(auctionId);
            var newBid = new Bid();
            newBid.setId(UUID.randomUUID());
            newBid.setOwnerId(bidderId);
            newBid.setAmount(bid);
            newBid.setTimestamp(new Date());
            newBid.setMaxBid(maxBid);

            this.bidManager.placeBid(auction, newBid);
            return new AppResponse<>(200, true, null, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        }
    }

    AppResponse<?> updateBid(UUID bidderId, UUID auctionId, double bid, double maxBid) {
        try {
            // Check that the bidder exists
            this.accountManager.get(bidderId);
            // Check that the auction exists
            var auction = this.auctionManager.get(auctionId);
            var newBid = new Bid();
            newBid.setOwnerId(bidderId);
            newBid.setAmount(bid);
            newBid.setTimestamp(new Date());
            newBid.setMaxBid(maxBid);

            this.bidManager.updateBid(auction, newBid);
            return new AppResponse<>(200, true, null, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        }
    }

    AppResponse<?> allCategories() {
        try {
            var categories = this.auctionManager.categories();
            var categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());
            return new AppResponse<>(200, true, categoryNames, "OK");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

}
