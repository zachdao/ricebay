package edu.rice.comp610.store;

import java.util.HashMap;
import java.util.Map;

public enum AuctionSortField {


    TITLE("title"),
    START_DATE("start_date"),
    END_DATE("end_date");

    AuctionSortField(String columnName) {
        this.columnName = columnName;
    }

    private String columnName;

    private static Map<String, AuctionSortField> columnMap=new HashMap<>();

    static {
        for (AuctionSortField sortField : AuctionSortField.values()) {
            columnMap.put(sortField.columnName, sortField);
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public static AuctionSortField getSortField(String sortField) {
        return columnMap.get(sortField);
    }
}
