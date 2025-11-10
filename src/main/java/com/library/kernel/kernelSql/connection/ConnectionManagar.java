package com.library.kernel.kernelSql.connection;

import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * Gestor estático de conexiones que delega en {@code ConnectionProvider}.
 *
 * Mantiene una única instancia estática de {@code ConnectionProvider} que
 * se inicializa mediante {@link #setDatabaseProperties(IDatabaseProperties)}.
 * Proporciona un método estático para obtener conexiones y convierte las
 * excepciones {@link SQLException} en {@link ConnectionManagerException} para
 * simplificar el manejo de errores a nivel de aplicación.
 */
public final class ConnectionManagar {
    /**
     * Proveedor compartido de conexiones. Se inicializ a solo una vez.
     */
    private static ConnectionProvider connectionProvider = null;
    /**
     * Establece las propiedades de la base de datos la primera vez que se llama.
     *
     * <p>Si {@code connectionProvider} ya ha sido inicializado, esta llamada no
     * tendrá efecto para evitar reconfiguraciones en tiempo de ejecución.</p>
     *
     * @param properties implementación de {@code IDatabaseProperties} con los
     *                   datos (host, puerto, base de datos, usuario, contraseña y driver)
     */
    public static void setDatabaseProperties(IDatabaseProperties properties){
        if(connectionProvider == null){
            connectionProvider = new ConnectionProvider(properties);
        }
    }
    /**
     * Obtiene una conexión a la base de datos desde el proveedor configurado.
     *
     * <p>Encapsula cualquier {@link SQLException} lanzada por el proveedor en una
     * {@link ConnectionManagerException} con contexto adicional.</p>
     *
     * @return instancia de {@link Connection} lista para usar
     * @throws ConnectionManagerException si no se puede obtener la conexión
     */
    public static Connection getConnection() throws ConnectionManagerException
    {
        try {
            // Delegar la obtención de la conexión al ConnectionProvider compartido
            return connectionProvider.getConnection();
        }catch (SQLException error) {
            Logger.errorLog("ConnectionManagerException", "ConnectionManager could not connect: " + error.getMessage());
            throw new ConnectionManagerException("ConnectionManager failed to connect to the database.", error);
        }
    }
}
