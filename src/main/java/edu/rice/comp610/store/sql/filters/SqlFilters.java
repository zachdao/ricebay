package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filter;
import edu.rice.comp610.model.Filters;

public class SqlFilters implements Filters {
    @Override
    public Filter makeEqualityFilter(String column) {
        return new SqlEqualityFilter(column);
    }

    @Override
    public Filter makeInFilter(String column, int size) {
        return new SqlInFilter(column, size);
    }

    @Override
    public Filter makeLessThanFilter(String column) {
        return new SqlLessThanFilter(column);
    }

    @Override
    public Filter makeLikeFilter(String column) {
        return new SqlLikeFilter(column);
    }

    @Override
    public Filter makeAndFilter(Filter... filters) {
        return new SqlAndFilter(filters);
    }

    @Override
    public Filter makeOrFilter(Filter... filters) {
        return new SqlOrFilter(filters);
    }
}
