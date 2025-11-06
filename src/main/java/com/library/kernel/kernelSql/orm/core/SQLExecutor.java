package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Ejecutador de sentencias SQL básicas sobre una conexión JDBC.
 *
 * Esta clase final implementa {@code ISQLExecutor} y proporciona métodos para:
 * - Ejecutar consultas SELECT y devolver el {@code ResultSet}.
 * - Ejecutar sentencias de actualización (INSERT/UPDATE/DELETE) y devolver el
 *   número de filas afectadas.
 *
 * Nota importante: esta clase no administra el ciclo de vida de la conexión
 * ni del {@code ResultSet} devuelto. El llamador es responsable de cerrar
 * recursos (ResultSet, Statement y Connection) según corresponda para evitar
 * fugas de recursos.
 */
final class SQLExecutor implements ISQLExecutor{
    /**
     * Ejecuta una consulta SELECT y devuelve el {@code ResultSet} resultante.
     *
     * @param connection Conexión JDBC a utilizar para ejecutar la consulta.
     * @param sqlStmt    Sentencia SQL de tipo SELECT a ejecutar.
     * @return {@code ResultSet} con los resultados de la consulta.
     * @throws SQLExecutorException Si ocurre un error SQL durante la ejecución.
     *
     * Nota: El {@code Statement} creado para ejecutar la consulta no se cierra
     * aquí, porque el {@code ResultSet} depende de él. El llamador debe cerrar
     * el {@code ResultSet} y el {@code Statement} cuando ya no sean necesarios.
     */
    @Override
    public ResultSet excuteSelectQuery(Connection connection, String sqlStmt) throws SQLExecutorException {
        try {

            // Se obtiene una declaración (Statement) desde la conexión
            Statement stmt = connection.createStatement();
            // Se ejecuta la consulta SELECT y se obtiene el ResultSet
            ResultSet resultSet = stmt.executeQuery(sqlStmt);

            return resultSet;

        } catch (SQLException error) {
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }
    /**
     * Ejecuta una sentencia de actualización SQL (INSERT, UPDATE, DELETE).
     *
     * @param connection Conexión JDBC utilizada para ejecutar la sentencia.
     * @param sqlStmt    Sentencia SQL de actualización a ejecutar.
     * @return Número de filas afectadas por la operación.
     * @throws SQLExecutorException Si ocurre un error SQL durante la ejecución.
     *
     * Este método usa try-with-resources para asegurar el cierre del {@code Statement}.
     */
    @Override
    public int executeUpdateStatment(Connection connection, String sqlStmt) throws SQLExecutorException {
        try {
            Statement stmt = connection.createStatement();
            // Se ejecuta la sentencia de actualización (INSERT/UPDATE/DELETE)
            int result = stmt.executeUpdate(sqlStmt);

            Logger.infoLog("SQL Query", sqlStmt);
            return result;

        } catch (SQLException error) {
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }

}
