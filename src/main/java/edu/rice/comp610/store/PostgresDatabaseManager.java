package edu.rice.comp610.store;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Manages loading and storing data in the database.
 */
public class PostgresDatabaseManager implements DatabaseManager {

    @Override
    public <T> List<T> loadObjects(Class<T> modelClass, String sql, Object... filterBy) {
        return List.of();
    }

    @Override
    public <T> void saveObjects(String sql, Field[] fields, T objects) {
    }

}
