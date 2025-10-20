package com.library.config;

import com.library.kernel.orm.SQLDriver;
import com.library.kernel.orm.DatabaseProperties;

public class DatabaseConfig extends DatabaseProperties {


    private static String host = "localhost";
    private static String port = "5420";
    private static String dataBaseName = "mydatabase";
    private static String dataBaseUser = "admin";
    private static String dataBasePassword = "admin123";
    private static SQLDriver sqlDriver = SQLDriver.POSTGRESQL;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getDataBaseName() {
        return dataBaseName;
    }

    @Override
    public String getDataBaseUser() {
        return dataBaseUser;
    }

    @Override
    public String getDataBasePassword() {
        return dataBasePassword;
    }

    @Override
    public String getSQLDriver() {
        return sqlDriver.getDriver();
    }
    
}
