package edu.rice.comp610.store;

import edu.rice.comp610.model.DatabaseManager;
import edu.rice.comp610.util.DatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages loading and storing data in the database.
 */
public class PostgresDatabaseManager implements DatabaseManager {
    private static PostgresDatabaseManager dbManager = null;

    public static void initialize(String jdbcUrl, Properties properties) {
        if (dbManager == null) {
            dbManager = new PostgresDatabaseManager(jdbcUrl, properties);
        }
    }

    public static void initialize(Properties properties) {
        if (dbManager == null) {
            dbManager = new PostgresDatabaseManager(properties);
        }
    }

    public static PostgresDatabaseManager getInstance() {
        return dbManager;
    }

    private final String jdbcUrl;
    private final Properties jdbcProperties;

    private PostgresDatabaseManager(String jdbcUrl, Properties jdbcProperties) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcProperties = jdbcProperties;
    }

    /**
     * Constructor that loads configuration from properties.
     * @param properties
     */
    private PostgresDatabaseManager(Properties properties) {
        boolean useEnvVarAsValue = Boolean.parseBoolean(properties.getProperty("ricebay.env", "false"));
        String jdbcUrl = properties.getProperty("ricebay.jdbc.url");
        String jdbcUser = properties.getProperty("ricebay.jdbc.user");
        String jdbcPassword = properties.getProperty("ricebay.jdbc.password");

        Properties jdbcProperties = new Properties();
        if (useEnvVarAsValue) {
            this.jdbcUrl = System.getenv(jdbcUrl);
            jdbcProperties.put("user", System.getenv(jdbcUser));
            jdbcProperties.put("password", System.getenv(jdbcPassword));
        } else {
            this.jdbcUrl = jdbcUrl;
            jdbcProperties.put("user", jdbcUser);
            jdbcProperties.put("password", jdbcPassword);
        }
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
                        PostgresQueryManager.Accessors accessors = query.accessorForColumn(name);
                        Object arg = results.getObject(columnIndex);
                        try {
                            accessors.invokeSetter(model, arg);
                        } catch (IllegalArgumentException e) {
                            throw new DatabaseException("Could not invoke " + accessors.setter.getName() + " for value "
                                    + arg + " of type " + typeOf(arg), e);
                        }
                    }
                    resultList.add(model);
                }
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
        return resultList;
    }

    private String typeOf(Object arg) {
        if (arg == null) {
            return "null";
        } else {
            return arg.getClass().getSimpleName();
        }
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
                        Object param = toSqlType(entry.getValue().invokeGetter(model));
                        statement.setObject(col, param);
                        col++;
                    }

                    if (query.isUpsert()) {
                        // UPDATE parameters (exclude one-to-many and primary key)
                        for (var entry : query.accessorsMap.entrySet()) {
                            if (entry.getValue().isOneToMany() || entry.getValue().isPrimaryKey()) {
                                continue;
                            }
                            Object param = toSqlType(entry.getValue().invokeGetter(model));
                            statement.setObject(col, param);
                            col++;
                        }
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
            e.printStackTrace();
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
