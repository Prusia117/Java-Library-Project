package com.library.kernel.enums;

public enum SQLDriver {
    
    MYSQL(
        "mysql"
    ),
    SQLITE(
        "mysql"
    ),
    ORACLESQL(
        "mysql"
    ),
    MARIADB(
        "mysql"
    ),
    SQLSERVER(
        "mysql"
    );

    private final String driverName;

    SQLDriver(String driverName){
        this.driverName = driverName;
    }

    public String getDriver(){
        return this.driverName;
    }

}
