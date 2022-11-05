package edu.rice.comp610.store;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseManager {
    /**
     * Loads objects of a specified type {@literal modelClass} from the database.
     *
     * @param query the query to run in order to load model objects from the database
     * @param filterBy   the list of parameters that are used in the SQL query, within the {@literal WHERE} class.
     * @param <T>        The type of model being loaded.
     * @return the list of results from the query.
     */
    <T> List<T> loadObjects(Query<T> query, Object... filterBy) throws SQLException;

    /**
     * Saves objects of a given type in the database.
     *
     * @param query   the query object to run to store data in the database; should be an {@literal INSERT}
     *                or {@literal UPDATE} statement.
     * @param objects The objects to store in the database. The fields list will be used to pull fields out of the
     *                object and pass them as parameters to the SQL statement.
     * @param <T>     The type of object being saved.
     */
    <T> void saveObjects(Query<T> query, T... objects) throws SQLException;
}
