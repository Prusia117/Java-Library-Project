package com.library.kernel.kernelSql.connection;

import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManagar {

    private static ConnectionProvider connectionProvider = null;

    public static void setDatabaseProperties(DatabaseProperties properties){
        if(connectionProvider == null){
            connectionProvider = new ConnectionProvider(properties);
        }
    }

    public static Connection getConnection() throws ConnectionManagerException
    {
        try {
            return connectionProvider.getConnection();
        }catch (SQLException error) {
            Logger.errorLog("ConnectionManagerException", "ConnectionManager could not connect: " + error.getMessage());
            throw new ConnectionManagerException("ConnectionManager failed to connect to the database.", error);
        }
    }
}
