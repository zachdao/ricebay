package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlAndFilterTest {

    @Test
    void makesAndExpression() {
        var expr = new SqlAndFilter(new SqlEqualityFilter("foo"), new SqlEqualityFilter("bar")).toQuery();
        assertEquals("(foo = ? AND bar = ?)", expr);
    }

    @Test
    void makesAndWithInnerOrExpression() {
        var expr = new SqlAndFilter(new SqlEqualityFilter("foo"), new SqlOrFilter(new SqlEqualityFilter("bar"), new SqlEqualityFilter("baz"))).toQuery();
        assertEquals("(foo = ? AND (bar = ? OR baz = ?))", expr);
    }

}
