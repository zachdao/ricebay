package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlInFilter implements Filter {
    private final String column;
    private final int size;

    SqlInFilter (String column, int size) {
        this.column = column;
        this.size = size;
    }

    @Override
    public String toQuery() {
        return String.format("%s IN (%s)", column, Stream.generate(() -> "?").limit(size).collect(Collectors.joining(",")));
    }
}
