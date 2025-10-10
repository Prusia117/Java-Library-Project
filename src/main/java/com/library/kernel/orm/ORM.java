package com.library.kernel.orm;

import com.library.kernel.classes.DatabaseParameters;
import com.library.kernel.enums.SQLDriver;

import java.sql.*;

public class ORM {

    private final SQLDriver driver = SQLDriver.MYSQL;

    private final DatabaseParameters databaseParameters = null;

    private Connection getConnection() throws SQLException
    {
        try{

            String stringConnection = "jdbc:" + driver.getDriver() + "://";
            Connection connection = DriverManager.getConnection(stringConnection, null, null);

            return connection;

        }catch(SQLException error){
            
            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ResultSet query(String sql) throws SQLException
    {

        try
        {
            Connection connection = this.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return resultSet;

        }catch(SQLException error){
            throw error;
        }
    
    }



}