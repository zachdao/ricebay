package edu.rice.comp610.model;

import edu.rice.comp610.store.Query;

public interface QueryManager {
    <T> Query<T> makeLoadQuery(Class<T> modelClass, String... filterBy);
    <T> Query<T> makeUpdateQuery(Class<T> modelClass);
}