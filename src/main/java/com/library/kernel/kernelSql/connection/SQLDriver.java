package com.library.kernel.kernelSql.connection;
/*
 * ENUM destinado a alamcenar los diferentes drivers de base de datos SQL
 */
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
