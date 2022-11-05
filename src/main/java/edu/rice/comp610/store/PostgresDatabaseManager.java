package edu.rice.comp610.store;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages loading and storing data in the database.
 */
public class PostgresDatabaseManager implements DatabaseManager {

    private final String jdbcUrl;
    private final Properties jdbcProperties;

    public PostgresDatabaseManager(String jdbcUrl, Properties jdbcProperties) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcProperties = jdbcProperties;
    }

    @Override
    public <T> List<T> loadObjects(Query<T> query, Object... filterBy)
            throws DatabaseException {
        ArrayList<T> resultList = new ArrayList<>();
        try {
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcProperties)) {
                PreparedStatement statement = connection.prepareStatement(query.getSql());
                for (int i = 0; i < filterBy.length; i++) {
                    statement.setObject(i + 1, filterBy[i]);
                }
                ResultSet results = statement.executeQuery();
                ResultSetMetaData metadata = statement.getMetaData();
                while (results.next()) {
                    T model = query.newModel();
                    for (int columnIndex = 1; columnIndex <= metadata.getColumnCount(); columnIndex++) {
                        String name = metadata.getColumnName(columnIndex);
                        Method setter = query.setterForColumn(name);
                        setter.invoke(model, results.getObject(columnIndex));
                    }
                    resultList.add(model);
                }
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new DatabaseException(e);
        }
        return resultList;
    }

    @Override
    public <T> void saveObjects(Query<T> query, T... objects) throws DatabaseException {
        try {
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcProperties)) {
                PreparedStatement statement = connection.prepareStatement(query.getSql());
                for (T model : objects) {
                    int col = 1;
                    // INSERT parameters (exclude one-to-many and generated PKs)
                    for (var entry : query.accessorsMap.entrySet()) {
                        if (entry.getValue().isOneToMany() || entry.getValue().isGenerated()) {
                            continue;
                        }
                        Object param = toSqlType(entry.getValue().getter.invoke(model));
                        statement.setObject(col, param);
                        col++;
                    }
                    // UPDATE parameters (exclude one-to-many and primary key)
                    for (var entry : query.accessorsMap.entrySet()) {
                        if (entry.getValue().isOneToMany() || entry.getValue().isPrimaryKey()) {
                            continue;
                        }
                        Object param = toSqlType(entry.getValue().getter.invoke(model));
                        statement.setObject(col, toSqlType(param));
                        col++;
                    }

                }
                if (statement.execute()) {
                    throw new IllegalStateException("Unexpected result set from update query: " + query.getSql());
                }

                int count = statement.getUpdateCount();
                if (count != objects.length) {
                    throw new IllegalStateException("Update count " + count + " from input data count "
                            + objects.length);
                }
            }
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new DatabaseException(e);
        }
    }

    private Object toSqlType(Object obj) {
        if (obj instanceof java.util.Date) {
            java.util.Date date = (java.util.Date)obj;
            return new java.sql.Date(date.getTime());
        }
        return obj;
    }

}
