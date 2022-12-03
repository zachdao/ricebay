package edu.rice.comp610.store.sql.filters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlLikeFilterTest {

    @Test
    void makesLikeExpression() {
        var expr = new SqlLikeFilter("foobar").toQuery();
        assertEquals("LOWER(foobar) LIKE CONCAT('%', ?, '%')", expr);
    }

}
