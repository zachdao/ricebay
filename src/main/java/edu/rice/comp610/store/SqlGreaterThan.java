package edu.rice.comp610.store;

import edu.rice.comp610.model.Filter;

public class SqlGreaterThan implements Filter {
    private final String column;

    SqlGreaterThan (String column) {
        this.column = column;
    }

    @Override
    public String toQuery() {
        return String.format("%s > ?", column);
    }
}
