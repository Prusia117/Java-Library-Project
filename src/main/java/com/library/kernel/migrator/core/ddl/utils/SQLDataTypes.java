package com.library.kernel.migrator.core.ddl.utils;

public enum SQLDataTypes {

    SERIAL(
            "SERIAL"
    ),
    INT(
            "INT"
    ),
    VARCHAR(
            "VARCHAR"
    ),
    BOOLEAN(
            "bool"
    ),
    DATE(
            "DATE"
    );

    private final String type;

    SQLDataTypes(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
