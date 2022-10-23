package edu.rice.comp610.store;

import java.lang.reflect.Field;

/**
 * Generates SQL queries for use with the {@link DatabaseManager}.
 *
 */
public class QueryManager {

    /**
     * Generate a SQL query to load model objects of a particular type. The model class is mapped to a database table
     * and class fields are mapped to columns that are used in a WHERE clause to filter the results.
     *
     * @param modelClass the model class that will be loaded.
     * @param filterBy the set of fields to filter by.
     * @return a SQL query string .
     */
    public String makeLoadQuery(Class<?> modelClass, Field[] filterBy) {
        return "";
    }

    /**
     * Generate a SQL query to insert or update model objects of a particular type. The model class is mapped to a
     * database table and class fields are mapped to columns that are used in a WHERE clause to filter the rows to
     * update.
     *
     * @param modelClass the model class that will be loaded.
     * @param filterBy the set of fields to filterBy
     * @return a SQL query string .
     */
    public String makeUpdateQuery(Class<?> modelClass, Field[] filterBy) {
        return "";
    }
}
