package edu.rice.comp610.controller;

import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Filter;
import edu.rice.comp610.model.Filters;
import edu.rice.comp610.store.AuctionSortField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a search query for {@link Auction}s.
 */
public class AuctionQuery {

    private final List<Filter> filters;
    private final List<Object> values;
    private final List<String> categories;
    private AuctionSortField sortField;
    private boolean sortAscending;

    private static final SimpleDateFormat parser = new SimpleDateFormat("MMM d, yyyy");

    public AuctionQuery() {
        this.filters = new ArrayList<>();
        this.values = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.sortField = AuctionSortField.TITLE;
        this.sortAscending = true;
    }

    public AuctionQuery(Filters filterFactory, Map<String, String[]> queryMap) {
        this.filters = new ArrayList<>();
        this.values = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.sortField = AuctionSortField.TITLE;
        this.sortAscending = true;
        for (Map.Entry<String, String[]> entry : queryMap.entrySet()) {
            var values = entry.getValue();
            var keyParts = entry.getKey().split(":");
            if (keyParts[0].equals("published")) {
                continue;
            } else if (keyParts[0].equals("hasCategories")) {
                this.categories.addAll(values.length > 0 ? List.of(values[0].split(",")) : List.of());
            } else if (keyParts[0].equals("sortBy")) {
                this.sortField = AuctionSortField.getSortField(values.length > 0 ? values[0] : "title");
            } else if (keyParts[0].equals("sortDescending")) {
                this.sortAscending = false;
            } else if (keyParts.length == 1 || keyParts[1].equals("eq")) {
                this.filters.add(filterFactory.makeEqualityFilter(keyParts[0]));
                this.values.add(values.length > 0 ? convertValue(values[0]) : null);
            } else if (keyParts[1].equals("like")) {
                this.filters.add(filterFactory.makeLikeFilter(keyParts[0]));
                this.values.add(values.length > 0 ? convertValue(values[0]) : "");
            } else if (keyParts[1].equals("in")) {
                this.filters.add(filterFactory.makeInFilter(keyParts[0], values.length));
                this.values.addAll(Stream.of(values).map(AuctionQuery::convertValue).collect(Collectors.toList()));
            }
        }
    }

    private static Object convertValue(String value) {
        if (value.matches("[tT]rue|[fF]alse")) {
            return Boolean.parseBoolean(value);
        } else if (value.matches("\\d+\\.\\d*")) {
            return Double.parseDouble(value);
        } else if (value.matches("\\d+")) {
            return Integer.parseInt(value);
        }

        try {
            return parser.parse(value);
        } catch (ParseException e) {
            // Not a date
        }

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            // Not a UUID
        }

        return value;
    }

    public Filter[] getFilters() {
        return filters.toArray(new Filter[0]);
    }

    public Object[] getValues() {
        return values.toArray();
    }

    public AuctionSortField getSortField() {
        return sortField;
    }

    public List<String> hasCategories() {
        return categories;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionQuery that = (AuctionQuery) o;
        return sortAscending == that.sortAscending && filters.equals(that.filters) && values.equals(that.values) && sortField == that.sortField;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filters, values, sortField, sortAscending);
    }

    @Override
    public String toString() {
        return "AuctionSearch{" +
                "queryText='" + filters + '\'' +
                "queryValues='" + values + '\'' +
                ", sortField=" + sortField +
                ", sortAscending=" + sortAscending +
                '}';
    }
}
