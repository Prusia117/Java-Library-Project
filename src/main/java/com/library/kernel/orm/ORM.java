package com.library.kernel.orm;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ORM {

    private static SQLDriver sqlDriver = SQLDriver.GENERIC;

    private static DatabaseProperties databaseProperties = null;

    private Connection getConnection() throws SQLException {
        try {

            DatabaseProperties databaseProperties = this.databaseProperties;

            String stringConnection = "jdbc:" + sqlDriver.getDriver() + "://";
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(), databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {

            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ResultSet selectQuery(Entity entity) throws SQLException {

        HashMap<String, String> map = EntityMapper.entityMapper(entity);

        for(Map.Entry<String, String> entry : map.entrySet()){
            
        }

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