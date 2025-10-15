package com.library.config;

import com.library.kernel.orm.SQLDriver;
import com.library.kernel.orm.DatabaseProperties;

public class databaseConfig extends DatabaseProperties {


    private String driver = "postgresql";
    private String host = "localhost";
    private String port = "5432";
    private String dataBaseName = "postgres";
    private String dataBaseUser = "";
    private String dataBasePassword = "";
    private SQLDriver sqlDriver = SQLDriver.MARIADB;

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
