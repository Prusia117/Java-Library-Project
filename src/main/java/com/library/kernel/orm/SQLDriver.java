package com.library.kernel.orm;

public enum SQLDriver {
    
    GENERIC(
        ""
    ),
    POSTGRESQL(
        "postgresql"
    ),
    MYSQL(
        "mysql"
    ),
    SQLITE(
        "sqlite"
    ),
    ORACLESQL(
        "oraclesql"
    ),
    MARIADB(
        "mariadb"
    ),
    SQLSERVER(
        "sqlserver"
    );

    private final String driverName;

    SQLDriver(String driverName){
        this.driverName = driverName;
    }

    public String getDriver(){
        return this.driverName;
    }

}
