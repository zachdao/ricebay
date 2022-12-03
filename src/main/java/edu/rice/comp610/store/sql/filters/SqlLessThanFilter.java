package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filter;

public class SqlLessThanFilter implements Filter {
    private final String column;

    SqlLessThanFilter(String column) {
        this.column = column;
    }

    @Override
    public String toQuery() {
        return String.format("%s < ?", column);
    }
}
