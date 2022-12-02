package edu.rice.comp610.model;

public interface Filters {
    Filter makeEqualityFilter(String column);

    Filter makeInFilter(String column);

    Filter makeGreaterThanFilter(String column);

    Filter makeLikeFilter(String column);
}
