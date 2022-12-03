package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlEqualityFilterTest {

    @Test
    void makesEqualityExpression() {
        var expr = new SqlEqualityFilter("foobar").toQuery();
        assertEquals("foobar = ?", expr);
    }

}
