package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Ejecutador de sentencias SQL parametrizadas sobre una conexión JDBC.
 *
 * Esta clase final implementa {@code ISQLExecutor} y proporciona métodos para:
 * - Ejecutar consultas SELECT y devolver el {@code ResultSet}.
 * - Ejecutar sentencias de actualización (INSERT/UPDATE/DELETE) y devolver el
 *   número de filas afectadas.
 *
 * Todas las sentencias se ejecutan con {@link PreparedStatement}: los valores se
 * enlazan como parámetros y nunca se interpretan como SQL.
 *
 * Nota importante: esta clase no administra el ciclo de vida de la conexión.
 * Al cerrar el {@code ResultSet} devuelto por un SELECT se cierra también su
 * {@code PreparedStatement}.
 */
final class SQLExecutor implements ISQLExecutor{
    /**
     * Ejecuta una consulta SELECT y devuelve el {@code ResultSet} resultante.
     *
     * @param connection Conexión JDBC a utilizar para ejecutar la consulta.
     * @param sqlStmt    Sentencia SELECT parametrizada a ejecutar.
     * @return {@code ResultSet} con los resultados de la consulta.
     * @throws SQLExecutorException Si ocurre un error al ejecutar la consulta.
     */
    @Override
    public ResultSet excuteSelectQuery(Connection connection, SQLStatement sqlStmt) throws SQLExecutorException {
        PreparedStatement stmt = null;
        try {

            // Se prepara la sentencia y se enlazan los valores
            stmt = prepare(connection, sqlStmt);
            // El statement se cierra automáticamente al cerrar el ResultSet
            stmt.closeOnCompletion();

            return stmt.executeQuery();

        } catch (SQLException error) {
            closeQuietly(stmt);
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }
    /**
     * Ejecuta una sentencia de actualización (INSERT, UPDATE o DELETE).
     *
     * @param connection Conexión JDBC a utilizar para ejecutar la sentencia.
     * @param sqlStmt    Sentencia de actualización parametrizada a ejecutar.
     * @return Número de filas afectadas por la operación.
     * @throws SQLExecutorException Si ocurre un error al ejecutar la sentencia.
     */
    @Override
    public int executeUpdateStatment(Connection connection, SQLStatement sqlStmt) throws SQLExecutorException {
        try (PreparedStatement stmt = prepare(connection, sqlStmt)) {
            // Se ejecuta la sentencia de actualización (INSERT/UPDATE/DELETE)
            int result = stmt.executeUpdate();

            Logger.infoLog("SQL Query", sqlStmt.sql());
            return result;

        } catch (SQLException error) {
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }

    private static PreparedStatement prepare(Connection connection, SQLStatement sqlStmt) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sqlStmt.sql());
        try {
            for (int i = 0; i < sqlStmt.params().size(); i++) {
                // Los índices de JDBC empiezan en 1
                stmt.setObject(i + 1, sqlStmt.params().get(i));
            }
            return stmt;
        } catch (SQLException error) {
            stmt.close();
            throw error;
        }
    }

    private static void closeQuietly(PreparedStatement stmt) {
        if (stmt == null) return;
        try {
            stmt.close();
        } catch (SQLException ignored) {
            // Ya se está propagando el error original
        }
    }

}
