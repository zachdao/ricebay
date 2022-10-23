package edu.rice.comp610.store;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Manages loading and storing data in the database.
 */
public class DatabaseManager {

    /**
     * Loads objects of a specified type {@literal modelClass} from the database.
     *
     * @param modelClass the type of objects to return
     * @param sql the SQL query to execute on the database; should be a {@literal SELECT} query
     * @param filterBy the list of parameters that are used in the SQL query, within the {@literal WHERE} class.
     * @return the list of results from the query.
     * @param <T> The type of model being loaded.
     */
    public <T> List<T> loadObjects(Class<T> modelClass, String sql, Object... filterBy) {
        return List.of();
    }

    /**
     * Saves objects of a given type in the database.
     *
     * @param sql the SQL query string that stores data in the database; should be an {@literal INSERT}
     *            or {@literal UPDATE} statement.
     * @param fields the set of fields used in the SQL statement as parameters
     * @param objects The objects to store in the database. The fields list will be used to pull fields out of the
     *                object and pass them as parameters to the SQL statement.
     * @param <T> The type of object being saved.
     */
    public <T> void saveObjects(String sql, Field[] fields, T objects) {
    }

}
