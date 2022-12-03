package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filter;

public class SqlEqualityFilter implements Filter {
    private final String column;

    SqlEqualityFilter (String column) {
        this.column = column;
    }

    @Override
    public String toQuery() {
        return String.format("%s = ?", column);
    }
}
