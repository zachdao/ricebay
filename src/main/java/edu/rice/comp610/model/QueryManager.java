package edu.rice.comp610.model;

import edu.rice.comp610.store.AuctionSortField;
import edu.rice.comp610.store.Query;

public interface QueryManager {
    <T> Query<T> makeLoadQuery(Class<T> modelClass);
    <T> Query<T> makeLoadQuery(Class<T> modelClass, Filter filterBy);
    <T> Query<T> makeLoadQuery(Class<T> modelClass, Filter filterBy, AuctionSortField sortBy);
    <T> Query<T> makeLoadQuery(Class<T> modelClass, Filter filterBy, AuctionSortField sortBy, Boolean sortAscending);
    // <T> Query<T> makeLoadQuery(Class<T> modelClass, AuctionSortField sortBy);
    // <T> Query<T> makeLoadQuery(Class<T> modelClass, AuctionSortField sortBy, Boolean sortAscending);
    <T> Query<T> makeUpdateQuery(Class<T> modelClass);
    <T> Query<T> makeUpdateQuery(Class<T> modelClass, boolean upsert);
    Filters filters();
}
