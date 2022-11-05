package edu.rice.comp610.store;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    final Map<String, QueryManager.Accessors> accessorsMap;

    /**
     *
     * @param modelClass the model class that this query operates on.
     * @param sql the raw SQL statement for this query.
     * @param params the field names of the model class that are used as the parameters for the SQL statement.
     */
    public Query(Class<T> modelClass, String sql, List<String> params, List<Object[]> values,
                 Map<String, QueryManager.Accessors> accessorsMap) {
        this.modelClass = modelClass;
        this.sql = sql;
        this.params = params;
        this.values = values;
        this.accessorsMap = accessorsMap;
    }

    public Query(Class<T> modelClass, String sql, String[] params, Map<String, QueryManager.Accessors> accessorsMap) {
        this(modelClass, sql, Arrays.asList(params), List.of(), accessorsMap);
    }
    public Query(Class<T> modelClass, String sql, List<String> params, Map<String, QueryManager.Accessors> accessorsMap) {
        this(modelClass, sql, params, List.of(), accessorsMap);
    }


    public String getSql() {
        return sql;
    }

    public T newModel() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return modelClass.getDeclaredConstructor().newInstance();
    }

    public Method setterForColumn(String name) {
        return accessorsMap.get(name).setter;
    }
}
