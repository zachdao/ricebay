package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlOrFilterTest {

    @Test
    void makesOrExpression() {
        var expr = new SqlOrFilter(new SqlEqualityFilter("foo"), new SqlEqualityFilter("bar")).toQuery();
        assertEquals("(foo = ? OR bar = ?)", expr);
    }

    @Test
    void makesOrWithInnerAndExpression() {
        var expr = new SqlOrFilter(new SqlEqualityFilter("foo"), new SqlAndFilter(new SqlEqualityFilter("bar"), new SqlEqualityFilter("baz"))).toQuery();
        assertEquals("(foo = ? OR (bar = ? AND baz = ?))", expr);
    }

}
