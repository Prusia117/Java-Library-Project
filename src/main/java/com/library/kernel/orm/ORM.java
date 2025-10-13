package com.library.kernel.orm;

import com.library.kernel.classes.DatabaseParameters;
import com.library.kernel.enums.SQLDriver;

import java.sql.*;
import java.util.Properties;

class ORM {

    private final SQLDriver driver = SQLDriver.MYSQL;

    private final DatabaseParameters databaseParameters = null;

    private static Properties getDataBaseProperties() {
        Properties p = new Properties();
        p.setProperty("user", null);
        p.setProperty("password", null);
        return p;
    }

    protected Connection getConnection() throws SQLException {
        try {

            Properties dataBaseProperties = getDataBaseProperties();

            String stringConnection = "jdbc:" + driver.getDriver() + "://";
            Connection connection = DriverManager.getConnection(stringConnection, dataBaseProperties);

            return connection;

        } catch (SQLException error) {

            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ResultSet getQuery(String sql) throws SQLException {

        try(
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
        ) 
        {
            return resultSet;

        } catch (SQLException error) {

            System.out.println("SQL Error Query Execution:" + error);
            throw error;
        }
    }

}