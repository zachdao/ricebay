package edu.rice.comp610.model;

public interface Filters {
    Filter makeEqualityFilter(String column);

    Filter makeInFilter(String column, int size);

    Filter makeLessThanFilter(String column);

    Filter makeLikeFilter(String column);

    Filter makeAndFilter(Filter... filters);

    Filter makeOrFilter(Filter... filters);
}
