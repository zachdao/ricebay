package edu.rice.comp610.store;

import edu.rice.comp610.model.Auction;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a search query for {@link Auction}s.
 */
public class AuctionQuery {

    private final Map<String, String[]> queryMap;
    private final AuctionSortField sortField;
    private final boolean sortAscending;

    public AuctionQuery(Map<String, String[]> queryMap, AuctionSortField sortField, boolean sortAscending) {
        this.queryMap = queryMap;
        this.sortField = sortField;
        this.sortAscending = sortAscending;
    }

    public Map<String, String[]> getQueryMap() {
        return queryMap;
    }

    public AuctionSortField getSortField() {
        return sortField;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionQuery that = (AuctionQuery) o;
        return sortAscending == that.sortAscending && queryMap.equals(that.queryMap) && sortField == that.sortField;
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryMap, sortField, sortAscending);
    }

    @Override
    public String toString() {
        return "AuctionSearch{" +
                "queryText='" + queryMap + '\'' +
                ", sortField=" + sortField +
                ", sortAscending=" + sortAscending +
                '}';
    }
}
