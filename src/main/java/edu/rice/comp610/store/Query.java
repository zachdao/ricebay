package edu.rice.comp610.store;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents a database query for a particular model object type.
 *
 * @param <T> the type of model object that this query will load or save.
 */
public class Query<T> {

    private final Class<T> modelClass;
    private final String sql;
    private final List<String> params;
    private final List<Object[]> values;
    private final boolean upsert;
    final Map<String, PostgresQueryManager.Accessors> accessorsMap;

    public Query() {
        this.modelClass = null;
        this.sql = null;
        this.params = null;
        this.values = null;
        this.accessorsMap = null;
        this.upsert = false;
    }

    /**
     *
     * @param modelClass the model class that this query operates on.
     * @param sql the raw SQL statement for this query.
     * @param params the field names of the model class that are used as the parameters for the SQL statement.
     */
    public Query(Class<T> modelClass, String sql, List<String> params, List<Object[]> values,
                 Map<String, PostgresQueryManager.Accessors> accessorsMap, boolean upsert) {
        this.modelClass = modelClass;
        this.sql = sql;
        this.params = params;
        this.values = values;
        this.accessorsMap = accessorsMap;
        this.upsert = upsert;
    }

    public Query(Class<T> modelClass, String sql, String[] params, Map<String, PostgresQueryManager.Accessors> accessorsMap) {
        this(modelClass, sql, Arrays.asList(params), List.of(), accessorsMap, false);
    }
    public Query(Class<T> modelClass, String sql, List<String> params, Map<String, PostgresQueryManager.Accessors> accessorsMap, boolean upsert) {
        this(modelClass, sql, params, List.of(), accessorsMap, upsert);
    }


    public String getSql() {
        return sql;
    }

    public boolean isUpsert() {
        return upsert;
    }

    public T newModel() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return modelClass.getDeclaredConstructor().newInstance();
    }

    public PostgresQueryManager.Accessors accessorForColumn(String name) {
        return accessorsMap.get(name);
    }
}
