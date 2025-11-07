package com.library.kernel.kernelSql.migrator.core.ddl.utils;
/**
 * Enum que representa tipos de datos SQL comunes utilizados por el migrador.
 * Cada valor contiene la representación textual del tipo que puede usarse
 * en la generación de DDL.
 */
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
