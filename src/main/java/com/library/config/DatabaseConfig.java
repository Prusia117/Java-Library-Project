package com.library.config;

import com.library.kernel.framework.EnvReader;
import com.library.kernel.kernelSql.connection.IDatabaseProperties;
import com.library.kernel.kernelSql.connection.SQLDriver;

public class DatabaseConfig implements IDatabaseProperties {


    private String host = EnvReader.getEnv("DATABASE_HOST");
    private String port = EnvReader.getEnv("DATABASE_PORT");
    private String dataBaseName = EnvReader.getEnv("DATABASE_NAME");
    private String dataBaseUser = EnvReader.getEnv("DATABASE_USER");
    private String dataBasePassword = EnvReader.getEnv("DATABASE_PASSWORD");
    private SQLDriver sqlDriver = SQLDriver.valueOf(EnvReader.getEnv("DATABASE_DRIVER"));

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
