package edu.rice.comp610.store;

import java.sql.*;
import java.util.*;

/**
 * Manages loading and storing data in the database.
 */
public class PostgresDatabaseManager implements DatabaseManager {

    @Override
    public <T> List<T> loadObjects(Query<T> query, Object... filterBy)
            throws SQLException {
        return List.of();
    }

    @Override
    public <T> void saveObjects(Query<T> query, T... objects) throws SQLException {
    }
}
