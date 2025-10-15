package com.library.kernel.orm;

import java.sql.*;
import java.util.Properties;

public class ORM {

    private static SQLDriver sqlDriver = SQLDriver.GENERIC;

    protected static DatabaseProperties databaseProperties = null;

    private static Properties getDataBaseProperties() {
        Properties p = new Properties();
        p.setProperty("user", null);
        p.setProperty("password", null);
        return p;
    }

    private Connection getConnection() throws SQLException {
        try {

            Properties dataBaseProperties = getDataBaseProperties();

            String stringConnection = "jdbc:" + sqlDriver.getDriver() + "://";
            Connection connection = DriverManager.getConnection(stringConnection, dataBaseProperties);

            return connection;

        } catch (SQLException error) {

            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ResultSet selectQuery(String sql) throws SQLException {

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

    public int updateQuery(String sql) throws SQLException {

        try(
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
        ) 
        {
            int result = stmt.executeUpdate(sql);
            return result;

        } catch (SQLException error) {

            System.out.println("SQL Error Query Execution:" + error);
            throw error;
        }
    }

    public static void setDatabaseProperties(DatabaseProperties properties){

        if(databaseProperties == null) databaseProperties = properties;

    }

}