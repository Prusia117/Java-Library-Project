package com.library.kernel.kernelSql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;

/**
 * Proveedor de conexiones JDBC para la aplicación.
 *
 * Esta clase crea conexiones a la base de datos utilizando las propiedades
 * provistas por una implementación de {@code IDatabaseProperties}. Mantiene
 * una referencia estática a las propiedades para que todas las instancias
 * compartan la misma configuración.
 */
final class ConnectionProvider  implements Colors {

    /**
     * Objeto que contiene las propiedades de conexión de la base de datos.
     * Ver la interfaz {@code IDatabaseProperties} para más información.
     */
    private static IDatabaseProperties databaseProperties = null;
    /**
     * Crea un proveedor de conexiones. Si aún no se han establecido las
     * propiedades estáticas, las asigna con la instancia proporcionada.
     *
     * @param properties implementación de {@code IDatabaseProperties} que contiene
     *                   host, puerto, nombre de base de datos, usuario, contraseña y driver
     */
    public ConnectionProvider(IDatabaseProperties properties){
        if(databaseProperties == null){
            databaseProperties = properties;
        }
    }

    /**
     * Obtiene una conexión JDBC usando el driver y credenciales definidos en
     * {@code databaseProperties}.
     *
     * Construye la URL con la forma:
     * jdbc:{driver}://{host}:{port}/{database}
     *
     * @return una conexión válida a la base de datos
     * @throws SQLException si falla la obtención de la conexión
     */    public Connection getConnection() throws SQLException {
        try {
            /*
             * Se crea el "string" de conexión con estructura:
             * jdbc:'driverName'://'host':'port'/'database'
             */
            String stringConnection = "jdbc:" + databaseProperties.getSQLDriver()
                    + "://" + databaseProperties.getHost() + ":" + databaseProperties.getPort() + "/"
                    + databaseProperties.getDataBaseName();

            // Se obtiene la conexión pasando URL, usuario y contraseña
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(),
                    databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {
            Logger.errorLog("ConnectionProviderException", "Database connection failed: " + error.getMessage());
            throw new SQLException("Failed to establish a database connection via ConnectionProvider.", error);
        }
    }
}
