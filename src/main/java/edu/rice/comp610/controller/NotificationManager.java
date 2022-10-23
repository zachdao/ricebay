package edu.rice.comp610.controller;

import edu.rice.comp610.model.Bid;
import edu.rice.comp610.model.Account;

/**
 * Controller that handles incoming requests for creating and viewing bids in the RiceBay system.
 */
public class NotificationManager {

    /**
     * <p>After a new bid is submitted and after each auction concludes,
     * notifications will be sent via email to: </p>
     * <ul>
     * <li> winning bidder </li>
     * <li> seller </li>
     * <li> other bidders </li>
     * </ul>
     *
     * <p> With the bid the auction and from there the other participants can be discovered </p>
     *
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see Bid
     */
    public AppResponse<NotificationManager> notifySeller(NotificationManager notificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

    public AppResponse<NotificationManager> notifyHighBid(NotificationManager notificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

    public AppResponse<NotificationManager> notifyOtherBid(NotificationManager notificationManager) {
        return new AppResponse<>(true, null, "OK");
    }

}
