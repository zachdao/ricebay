package edu.rice.comp610.store;

import edu.rice.comp610.model.DatabaseManager;
import edu.rice.comp610.model.Filter;
import edu.rice.comp610.model.Filters;
import edu.rice.comp610.model.QueryManager;
import edu.rice.comp610.util.Util;
import org.postgresql.util.PGmoney;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates SQL queries for use with the {@link DatabaseManager}.
 *
 */
public class PostgresQueryManager implements QueryManager {

    private static final Set<String> IGNORE = Set.of("getClass");
    private final Filters filters = new SqlFilters();

    static final class Accessors {
        Method getter;
        Method setter;
        boolean isOneToMany() {
            return getter.isAnnotationPresent(OneToMany.class) || setter.isAnnotationPresent(OneToMany.class);
        }
        boolean isPrimaryKey() {
            return getter.isAnnotationPresent(PrimaryKey.class) || setter.isAnnotationPresent(PrimaryKey.class);
        }

        boolean isGenerated() {
            return (getter.isAnnotationPresent(PrimaryKey.class) && getter.getAnnotation(PrimaryKey.class).generated())
                    || (setter.isAnnotationPresent(PrimaryKey.class) && setter.getAnnotation(PrimaryKey.class).generated());
        }

        /**
         * Invoke the getter method on a model, converting to SQL type of the result as necessary.
         * @param model the model
         * @return the result of the getter, converted if necessary
         * @throws InvocationTargetException if there is an exception thrown by the getter
         * @throws IllegalAccessException if the caller does not have access to the getter
         */
        Object invokeGetter(Object model) throws InvocationTargetException, IllegalAccessException {
            Object result = getter.invoke(model);
            if (getter.isAnnotationPresent(SqlType.class)) {
                return toSqlType(result, getter.getAnnotation(SqlType.class).value());
            } else {
                return result;
            }
        }

        /**
         * Invoke the setter method on a model, converting to SQL type of the parameter as necessary.
         * @param model the model
         * @param value the parameter to the setter method
         * @throws InvocationTargetException if there is an exception thrown by the setter
         * @throws IllegalAccessException if the caller does not have access to the setter
         */
        void invokeSetter(Object model, Object value) throws InvocationTargetException, IllegalAccessException {
            if (setter.isAnnotationPresent(SqlType.class)) {
                value = toSqlType(value, setter.getAnnotation(SqlType.class).value());
            }
            setter.invoke(model, value);
        }

        private Object toSqlType(Object from, Class<?> toType) {
            if (from.getClass() == toType) {
                return from;
            } else if (from instanceof Double && toType == PGmoney.class) {
                return new PGmoney((Double) from);
            } else {
                throw new IllegalArgumentException("Cannot convert type " + from.getClass() + " to SQL type " + toType);
            }
        }
    }

    static <T> Map<String, Accessors> makeColumnsToAccessorsMap(Class<T> modelClass)
    {
        Map<String, Accessors> result = new TreeMap<>();
        for (Method method : modelClass.getMethods()) {
            if (IGNORE.contains(method.getName())) {
                continue;
            }
            if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                String column = toColumn(method.getName().substring(3));
                result.putIfAbsent(column, new Accessors());
                Accessors accessors = result.get(column);
                accessors.setter = method;
            }
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                String column = toColumn(method.getName().substring(3));
                result.putIfAbsent(column, new Accessors());
                Accessors accessors = result.get(column);
                accessors.getter = method;
            }
        }
        result.entrySet().forEach(
                entry -> {
                    if (entry.getValue().setter == null) {
                        throw new IllegalStateException("No setter for field " + entry.getKey());
                    }
                    if (entry.getValue().getter == null) {
                        throw new IllegalStateException("No getter for field " + entry.getKey());
                    }
                }
        );
        return result;
    }

    private static String toColumn(String methodName) {
        return methodName.replaceAll("([A-Z])", "_$1").toLowerCase().substring(1);
    }

    public PostgresQueryManager() {

    }

    /**
     * Generate a SQL query to load model objects of a particular type. The model class is mapped to a database table
     * and class fields are mapped to columns that are used in a WHERE clause to filter the results.
     *
     * @param modelClass the model class that will be loaded.
     * @param filterBy the set of fields to filter by.
     * @return a SQL query string .
     */
    public <T> Query<T> makeLoadQuery(Class<T> modelClass, Filter... filterBy) {
        Map<String, Accessors> accessorsMap = makeColumnsToAccessorsMap(modelClass);
        String primaryTable = Util.getInstance().camelToSnake(modelClass.getSimpleName());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        stringBuilder.append(accessorsMap.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getter.isAnnotationPresent(OneToMany.class)
                        && !entry.getValue().setter.isAnnotationPresent(OneToMany.class))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", ")));
        stringBuilder.append(" FROM ").append(primaryTable);
        if (filterBy.length > 0 ) {
            stringBuilder.append(" WHERE ");
            boolean first = true;
            for (Filter filter : filterBy) {
                if (first)
                    first = false;
                else
                    stringBuilder.append(" AND ");
                stringBuilder.append(filter.toQuery());
            }
        }

        String sql = stringBuilder.toString();
        return new Query<>(modelClass, sql, accessorsMap, false);
    }

    /**
     * Generate a SQL query to insert or update model objects of a particular type. The model class is mapped to a
     * database table and class fields are mapped to columns that are used in a WHERE clause to filter the rows to
     * update.
     *
     * @param modelClass the model class that will be loaded.
     * @return a SQL query string .
     */
    public <T> Query<T> makeUpdateQuery(Class<T> modelClass) {
        return makeUpdateQuery(modelClass, true);
    }

    /**
     * Generate a SQL query to insert or update model objects of a particular type. The model class is mapped to a
     * database table and class fields are mapped to columns that are used in a WHERE clause to filter the rows to
     * update.
     *
     * @param modelClass the model class that will be loaded.
     * @param upsert flag to control if we handle ON CONFLICT
     * @return a SQL query string .
     */
    public <T> Query<T> makeUpdateQuery(Class<T> modelClass, boolean upsert) {
        Map<String, Accessors> accessorsMap = makeColumnsToAccessorsMap(modelClass);
        String primaryTable = Util.getInstance().camelToSnake(modelClass.getSimpleName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");
        stringBuilder.append(primaryTable);
        stringBuilder.append(" (");

        List<String> insertColumns = accessorsMap.entrySet().stream()
                .filter(entry -> !entry.getValue().isGenerated() && !entry.getValue().isOneToMany())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        stringBuilder.append(String.join(", ", insertColumns));
        stringBuilder.append(")");
        stringBuilder.append(" VALUES (");
        stringBuilder.append(insertColumns.stream().map(key -> "?").collect(Collectors.joining(", ")));

        // Compute the list of update columns
        List<String> updateColumns = accessorsMap.entrySet().stream()
                .filter(entry -> !entry.getValue().isPrimaryKey() && !entry.getValue().isOneToMany())
                .map(entry -> entry.getKey() + " = ?")
                .collect(Collectors.toList());
        if (upsert) {
            stringBuilder.append(") ON CONFLICT (");
            List<String> pkColumns = accessorsMap.entrySet().stream()
                    .filter(entry -> entry.getValue().isPrimaryKey())
                    .map(Map.Entry::getKey).collect(Collectors.toList());
            if (pkColumns.isEmpty()) {
                throw new IllegalStateException("Model class has no fields marked with @PrimaryKey");
            }
            stringBuilder.append(String.join(", ", pkColumns));
            stringBuilder.append(") DO UPDATE SET ");
            stringBuilder.append(String.join(", ", updateColumns));
        } else {
            stringBuilder.append(")");
        }
        String sql = stringBuilder.toString();
        List<String> params = Stream.concat(insertColumns.stream(), updateColumns.stream()).collect(Collectors.toList());
        return new Query<>(modelClass, sql, accessorsMap, true);
    }

    @Override
    public Filters filters() {
        return this.filters;
//        List<String> params = insertColumns;
//        if (upsert) {
//            params = Stream.concat(insertColumns.stream(), updateColumns.stream()).collect(Collectors.toList());
//        }
//        return new Query<>(modelClass, sql, params, accessorsMap, upsert);
    }
}
