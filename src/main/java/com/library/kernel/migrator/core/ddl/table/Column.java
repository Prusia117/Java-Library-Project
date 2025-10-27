package com.library.kernel.migrator.core.ddl.table;

import com.library.kernel.migrator.core.ddl.utils.SQLDataTypes;

public final class Column {

    private String columName;
    private SQLDataTypes columnType;
    private int columnSize;
    private Boolean nullable = false;

    public Column(String columName, SQLDataTypes columnType){
        this.columName = columName;
        this.columnType = columnType;
    }

    public void setColumnSize(int columnSize){
        this.columnSize = columnSize;
    }

    public void setNullable(boolean nullable){
        this.nullable = nullable;
    }

    public String getColumName(){
        return columName;
    }

    public String toString(){
        return columName + " " + columnType + (columnSize != 0 ? ("(" + columnSize + ") ") : " ") + (!nullable ? "NOT NULL" : "NULL");
    }
}
