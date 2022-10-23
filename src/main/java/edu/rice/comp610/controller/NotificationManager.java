package edu.rice.comp610.controller;

import edu.rice.comp610.model.Bid;
import edu.rice.comp610.model.Account;

public class NotificationManager {

    /**
     * After a new bid is submitted and after each auction concludes,
     * notifications will be sent via email to:
     * winning bidder
     * seller
     * other bidders
     *
     * With the bid the auction and from there the other participants can be discovered
     *
     * @param bid the latest bid id or bid object.
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see Bid
     */
    public AppResponse<NotificationManager> notifySeller(NotificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

    public AppResponse<NotificationManager> notifyHighBid(NotificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

    public AppResponse<NotificationManager> notifyOtherBid(NotificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

}
