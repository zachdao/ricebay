package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlLessThanFilterTest {

    @Test
    void makesGreaterThanExpression() {
        var expr = new SqlLessThanFilter("foobar").toQuery();
        assertEquals("foobar < ?", expr);
    }

}
