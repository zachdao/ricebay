package edu.rice.comp610.store;

import edu.rice.comp610.model.Filter;

public class SqlLikeFilter implements Filter {
    private final String column;

    SqlLikeFilter (String column) {
        this.column = column;
    }
    @Override
    public String toQuery(){
        return String.format("%s LIKE ?", column);
    }
}
