package edu.rice.comp610.store;

import edu.rice.comp610.model.Auction;

import java.util.Objects;

/**
 * Represents a search query for {@link Auction}s.
 */
public class AuctionQuery {

    private final String queryText;
    private final AuctionSortField sortField;
    private final boolean sortAscending;

    public AuctionQuery(String queryText, AuctionSortField sortField, boolean sortAscending) {
        this.queryText = queryText;
        this.sortField = sortField;
        this.sortAscending = sortAscending;
    }

    public String getQueryText() {
        return queryText;
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
        return sortAscending == that.sortAscending && queryText.equals(that.queryText) && sortField == that.sortField;
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryText, sortField, sortAscending);
    }

    @Override
    public String toString() {
        return "AuctionSearch{" +
                "queryText='" + queryText + '\'' +
                ", sortField=" + sortField +
                ", sortAscending=" + sortAscending +
                '}';
    }
}
