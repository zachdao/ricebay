package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.store.AuctionSortField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a search query for {@link Auction}s.
 */
public class AuctionQuery {

    private final Map<String, Object[]> queryMap;
    private final AuctionSortField sortField;
    private final boolean sortAscending;

    private static SimpleDateFormat parser = new SimpleDateFormat("MMM d, yyyy");

    public AuctionQuery() {
        this.queryMap = Collections.emptyMap();
        this.sortField = AuctionSortField.TITLE;
        this.sortAscending = true;
    }

    public AuctionQuery(Map<String, String[]> queryMap, AuctionSortField sortField, boolean sortAscending) {
        this.queryMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : queryMap.entrySet()) {
            List<Object> queryValues = new ArrayList<>();
            for (String queryValue : entry.getValue()) {
                if (queryValue.matches("[tT]rue|[fF]alse")) {
                    queryValues.add(Boolean.parseBoolean(queryValue));
                } else if (queryValue.matches("\\d+\\.\\d*")) {
                    queryValues.add(Double.parseDouble(queryValue));
                } else if (queryValue.matches("\\d+")) {
                    queryValues.add(Integer.parseInt(queryValue));
                } else {
                    try {
                        queryValues.add(parser.parse(queryValue));
                    } catch (ParseException e) {
                        queryValues.add(queryValue);
                    }
                }
            }
            this.queryMap.put(entry.getKey(), queryValues.toArray());
        }
        this.sortField = sortField;
        this.sortAscending = sortAscending;
    }

    public Map<String, Object[]> getQueryMap() {
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
