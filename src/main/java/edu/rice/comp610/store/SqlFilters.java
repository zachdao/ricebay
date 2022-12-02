package edu.rice.comp610.store;

import edu.rice.comp610.model.Filter;
import edu.rice.comp610.model.Filters;

public class SqlFilters implements Filters {
    @Override
    public Filter makeEqualityFilter(String column) {
        return new SqlEqualityFilter(column);
    }

    @Override
    public Filter makeInFilter(String column) {
        return new SqlInFilter(column);
    }

    @Override
    public Filter makeGreaterThanFilter(String column) {
        return new SqlGreaterThan(column);
    }

    @Override
    public Filter makeLikeFilter(String column) {
        return new SqlLikeFilter(column);
    }
}
