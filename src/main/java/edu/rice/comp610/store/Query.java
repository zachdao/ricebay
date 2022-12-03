package edu.rice.comp610.store;

import java.lang.reflect.InvocationTargetException;
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
    private final boolean upsert;
    final Map<String, PostgresQueryManager.Accessors> accessorsMap;

    public Query() {
        this.modelClass = null;
        this.sql = null;
        this.accessorsMap = null;
        this.upsert = false;
    }

    /**
     * @param modelClass the model class that this query operates on.
     * @param sql        the raw SQL statement for this query.
     */
    public Query(Class<T> modelClass, String sql,
                 Map<String, PostgresQueryManager.Accessors> accessorsMap, boolean upsert) {
        this.modelClass = modelClass;
        this.sql = sql;
        this.accessorsMap = accessorsMap;
        this.upsert = upsert;
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
