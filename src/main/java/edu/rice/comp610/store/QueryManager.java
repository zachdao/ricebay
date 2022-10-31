package edu.rice.comp610.store;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates SQL queries for use with the {@link DatabaseManager}.
 *
 */
public class QueryManager {

    private static final Set<String> IGNORE = Set.of("getClass");

    static final class Accessors {
        Method getter;
        Method setter;
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
                result.get(column).setter = method;
            }
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                String column = toColumn(method.getName().substring(3));
                result.putIfAbsent(column, new Accessors());
                result.get(column).getter = method;
            }
        }
        return result;
    }

    private static String toColumn(String methodName) {
        return methodName.replaceAll("([A-Z])", "_$1").toLowerCase().substring(1);
    }

    QueryManager() {

    }

    /**
     * Generate a SQL query to load model objects of a particular type. The model class is mapped to a database table
     * and class fields are mapped to columns that are used in a WHERE clause to filter the results.
     *
     * @param modelClass the model class that will be loaded.
     * @param filterBy the set of fields to filter by.
     * @return a SQL query string .
     */
    public String makeLoadQuery(Class<?> modelClass, String... filterBy) {
        Map<String, Accessors> accessorsMap = makeColumnsToAccessorsMap(modelClass);
        String primaryTable = modelClass.getSimpleName().toLowerCase();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        stringBuilder.append(accessorsMap.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getter.isAnnotationPresent(OneToMany.class)
                        && !entry.getValue().setter.isAnnotationPresent(OneToMany.class))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", ")));
        stringBuilder.append(" FROM ").append(primaryTable);
        stringBuilder.append(" WHERE ");
        boolean first = true;
        for (String column : filterBy) {
            if (first)
                first = false;
            else
                stringBuilder.append(" AND ");
            stringBuilder.append(column).append(" = ?");
        }
        return stringBuilder.toString();
    }

    /**
     * Generate a SQL query to insert or update model objects of a particular type. The model class is mapped to a
     * database table and class fields are mapped to columns that are used in a WHERE clause to filter the rows to
     * update.
     *
     * @param modelClass the model class that will be loaded.
     * @return a SQL query string .
     */
    public String makeUpdateQuery(Class<?> modelClass) {
        Map<String, Accessors> accessorsMap = makeColumnsToAccessorsMap(modelClass);
        String primaryTable = modelClass.getSimpleName().toLowerCase();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");
        stringBuilder.append(primaryTable);
        stringBuilder.append(" (");

        List<String> insertColumns = accessorsMap.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getter.isAnnotationPresent(OneToMany.class)
                        && !entry.getValue().setter.isAnnotationPresent(OneToMany.class))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        stringBuilder.append(String.join(", ", insertColumns));
        stringBuilder.append(")");
        stringBuilder.append(" VALUES (");
        stringBuilder.append(insertColumns.stream().map(key -> "?").collect(Collectors.joining(", ")));

        // Compute the list of update columns
        String updateColumns = accessorsMap.entrySet().stream()
                .filter(entry ->
                        !entry.getValue().getter.isAnnotationPresent(PrimaryKey.class)
                        && !entry.getValue().setter.isAnnotationPresent(PrimaryKey.class)
                        && !entry.getValue().getter.isAnnotationPresent(OneToMany.class)
                        && !entry.getValue().setter.isAnnotationPresent(OneToMany.class))
                .map(entry -> entry.getKey() + " = ?")
                .collect(Collectors.joining(", "));
        stringBuilder.append(") ON CONFLICT (");
        stringBuilder.append(accessorsMap.entrySet().stream()
                .filter(entry -> entry.getValue().setter.isAnnotationPresent(PrimaryKey.class)
                        || entry.getValue().getter.isAnnotationPresent(PrimaryKey.class)).collect(Collectors.toList())
                .get(0).getKey());
        stringBuilder.append(") UPDATE ");
        stringBuilder.append(updateColumns);
        return stringBuilder.toString();
    }
}
