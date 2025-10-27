package com.library.kernel.kernelSql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;


final class ConnectionProvider  implements Colors, KernelConnection
{
    
    // Object tipo DatabaseProperties consulta su interfaz par mas información
    private static DatabaseProperties databaseProperties = null;

    public ConnectionProvider(DatabaseProperties properties){
        if(databaseProperties == null){
            databaseProperties = properties;
        }
    }

    // Método para obtener una conexión utilizando el driver de postgres JDBC https://jdbc.postgresql.org/
    public Connection getConnection() throws SQLException {
        try {
            /*
             * Se crea el "string" de conexión con estructura:
             * jdbc:'driverName'://'host':'port'/'database'
             */
            String stringConnection = "jdbc:" + databaseProperties.getSQLDriver()
                    + "://" + databaseProperties.getHost() + ":" + databaseProperties.getPort() + "/"
                    + databaseProperties.getDataBaseName();

            // Se crea una conexión y se le pasa al método getConnection el string conexión como otros parametros como el usario y contraseña
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(),
                    databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {
            Logger.errorLog("SQL Connection Provider Error", error.getMessage());
            return null;
        }
    }
}
