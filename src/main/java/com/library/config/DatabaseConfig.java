package com.library.config;

import com.library.kernel.orm.core.connection.DatabaseProperties;
import com.library.kernel.orm.core.connection.SQLDriver;

public class DatabaseConfig implements DatabaseProperties {


    private String host = "localhost";
    private String port = "5420";
    private String dataBaseName = "mydatabase";
    private String dataBaseUser = "admin";
    private String dataBasePassword = "admin123";
    private SQLDriver sqlDriver = SQLDriver.POSTGRESQL;

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
