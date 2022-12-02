package edu.rice.comp610.store;

import edu.rice.comp610.model.Filter;

public class SqlInFilter implements Filter {
    private final String column;

    SqlInFilter (String column) {
        this.column = column;
    }

    @Override
    public String toQuery() {
        return String.format("%s IN ?", column);
    }
}
