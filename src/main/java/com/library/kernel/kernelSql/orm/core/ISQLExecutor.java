package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;

import java.sql.Connection;
import java.sql.ResultSet;
/**
 * Interfaz que define las operaciones básicas para ejecutar sentencias SQL
 * sobre una conexión JDBC.
 *
 * Esta interfaz expone métodos para:
 * - Ejecutar consultas de selección (SELECT) y obtener un {@code ResultSet}.
 * - Ejecutar sentencias de actualización (INSERT, UPDATE, DELETE) y obtener
 *   el número de filas afectadas.
 *
 * Nota: la gestión del ciclo de vida de la {@code Connection}, del {@code ResultSet}
 * y de los {@code Statement} corresponde al llamador. Los métodos lanzan
 * {@link SQLExecutorException} en caso de errores de acceso a la base de datos.
 */
abstract interface ISQLExecutor {

    /**
     * Ejecuta una consulta SELECT utilizando la conexión proporcionada.
     *
     * @param connection Conexión JDBC a utilizar para ejecutar la consulta.
     * @param sqlStmt    Sentencia SELECT parametrizada a ejecutar.
     * @return {@code ResultSet} con los resultados de la consulta.
     * @throws SQLExecutorException Si ocurre un error durante la ejecución SQL.
     *
     * Nota: el {@code ResultSet} devuelto depende del {@code Statement} creado;
     * el llamador debe cerrar ambos cuando termine su uso.
     */
    public abstract ResultSet excuteSelectQuery(Connection connection, SQLStatement sqlStmt) throws SQLExecutorException;
    /**
     * Ejecuta una sentencia de actualización (INSERT, UPDATE o DELETE).
     *
     * @param connection Conexión JDBC a utilizar para ejecutar la sentencia.
     * @param sqlStmt    Sentencia de actualización parametrizada a ejecutar.
     * @return Número de filas afectadas por la operación.
     * @throws SQLExecutorException Si ocurre un error durante la ejecución SQL.
     *
     * Este método puede cerrar internamente el {@code Statement}; el comportamiento
     * concreto depende de la implementación.
     */
    public abstract int executeUpdateStatment(Connection connection, SQLStatement sqlStmt) throws SQLExecutorException;

}
