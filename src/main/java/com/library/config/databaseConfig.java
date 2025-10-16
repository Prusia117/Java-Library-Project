package com.library.config;

import com.library.kernel.orm.SQLDriver;
import com.library.kernel.orm.DatabaseProperties;

public class DatabaseConfig extends DatabaseProperties {


    private static String driver = "postgresql";
    private static String host = "localhost";
    private static String port = "5432";
    private static String dataBaseName = "postgres";
    private static String dataBaseUser = "";
    private static String dataBasePassword = "";
    private static SQLDriver sqlDriver = SQLDriver.MARIADB;

    @Override
    public String getDriver() {
        return driver;
    }

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
    public SQLDriver getSQLDriver() {
        return sqlDriver;
    }
    
}
