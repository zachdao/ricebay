package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlInFilterTest {

    @Test
    void makesInExpressionSize0() {
        var expr = new SqlInFilter("foobar", 0).toQuery();
        assertEquals("foobar IN ()", expr);
    }

    @Test
    void makesInExpressionSize1() {
        var expr = new SqlInFilter("foobar", 1).toQuery();
        assertEquals("foobar IN (?)", expr);
    }

    @Test
    void makesInExpressionSize2() {
        var expr = new SqlInFilter("foobar", 2).toQuery();
        assertEquals("foobar IN (?,?)", expr);
    }

}
