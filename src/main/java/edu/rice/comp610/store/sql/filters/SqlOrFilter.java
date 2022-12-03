package edu.rice.comp610.store.sql.filters;

import edu.rice.comp610.model.Filter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SqlOrFilter implements Filter {
    private final Filter[] filters;

    SqlOrFilter(Filter... filters) {
        this.filters = filters;
    }

    @Override
    public String toQuery() {
        return "(" + Arrays.stream(filters).map(Filter::toQuery).collect(Collectors.joining(" OR ")) + ")";
    }
}
