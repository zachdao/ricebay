package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlFiltersTest {

    private final Filters filters = new SqlFilters();

    @Test
    void makesEqualityFilter() {
        var filter = filters.makeEqualityFilter("foobar");
        assertEquals(SqlEqualityFilter.class, filter.getClass());
    }

    @Test
    void makesLessThanFilter() {
        var filter = filters.makeLessThanFilter("foobar");
        assertEquals(SqlLessThanFilter.class, filter.getClass());
    }

    @Test
    void makesInFilter() {
        var filter = filters.makeInFilter("foobar", 1);
        assertEquals(SqlInFilter.class, filter.getClass());
    }

    @Test
    void makesLikeFilter() {
        var filter = filters.makeLikeFilter("foobar");
        assertEquals(SqlLikeFilter.class, filter.getClass());
    }

    @Test
    void makesAndFilter() {
        var filter = filters.makeAndFilter();
        assertEquals(SqlAndFilter.class, filter.getClass());
    }

    @Test
    void makesOrFilter() {
        var filter = filters.makeOrFilter();
        assertEquals(SqlOrFilter.class, filter.getClass());
    }
}
