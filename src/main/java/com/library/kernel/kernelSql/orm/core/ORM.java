package com.library.kernel.kernelSql.orm.core;

import java.sql.*;
import java.util.ArrayList;

import com.library.kernel.classes.Colors;
import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.entity.utils.EntityMapperException;

/**
 * ORM generic para operaciones básicas sobre entidades que extienden {@link Entity}.
 *
 * El parámetro genérico {@code T} representa una implementación concreta de {@code Entity}.
 * Esta clase proporciona métodos para ejecutar consultas SELECT, INSERT/UPDATE y DELETE
 * utilizando un constructor de SQL y un ejecutor de sentencias.
 */
public final class ORM<T extends Entity> implements IORMEngine<T>, Colors {
    // Ejecutores y constructores de sentencias SQL

    private final ISQLExecutor sqlExecutor;
    private final ISQLBuilder sqlBuilder;

    // Inicialización por defecto de los componentes SQL
    {
        sqlExecutor = new SQLExecutor();
        sqlBuilder = new SQLBuilder();
    }

    /**
     * Obtiene una conexión de la capa de conexión.
     *
     * @return una conexión válida a la base de datos
     * @throws ORMException si no se puede obtener la conexión
     */
    private static Connection getConnection() throws ORMException {
        try {
            return ConnectionManagar.getConnection();
        } catch (ConnectionManagerException error) {
            Logger.errorLog("ORMException", "ORM failed to retrieve connection: " + error.getMessage());
            throw new ORMException("ORM could not obtain a valid database connection.", error);
        }
    }



    /**
     * Ejecuta una consulta SELECT para la entidad proporcionada.
     *
     * <p>Construye la sentencia SQL mediante {@code sqlBuilder} y mapea el {@code ResultSet}
     * a una lista de entidades del tipo {@code T} usando {@code SQLResultMapper}.</p>
     *
     * @param entity entidad que define la tabla y el mapeo (no puede ser {@code null})
     * @param value  valor para filtrar (opcional; si es {@code null} puede devolver todos los registros)
     * @param field  campo sobre el que aplicar el filtro (opcional; se usa junto a {@code value})
     * @return lista de entidades mapeadas desde el resultado de la consulta
     * @throws ORMException si ocurre un error al obtener la conexión, ejecutar la consulta o mapear resultados
     */
    @Override
    public ArrayList<T> selectQuery(T entity, String value, String field) throws ORMException
    {
        String sqlStmt = null;
        try {

            if(entity == null){
                throw new ORMException("Invalid Values Provided");
            }

            sqlStmt = sqlBuilder.createSelectQuery(entity, value, field);

            Logger.infoLog("SQL Query", sqlStmt);
            try (
                    Connection conn = getConnection();
                    ResultSet resultSet = sqlExecutor.excuteSelectQuery(conn, sqlStmt);
            ){
                ArrayList<T> entityArray = SQLResultMapper.mapResultSetToEntities(resultSet, entity);
                return entityArray;
            }

        } catch (Exception error) {
            Logger.infoLog("SQL Query", sqlStmt);
            Logger.errorLog("Unhandled SQL Exception", error.getMessage());
            throw new ORMException("Unhandled SQL exception during query execution: " + error.getMessage(), error);
        }
    }
    /**
     * Ejecuta una sentencia de actualización (INSERT, UPDATE o DELETE).
     *
     * @param entity entidad asociada (se usa para trazas/logs; no es modificada)
     * @param sqlStmt sentencia SQL a ejecutar
     * @return número de filas afectadas por la operación
     * @throws ORMException si hay un error en la ejecución SQL o al obtener la conexión
     */
    private int executeUpdate(T entity, String sqlStmt) throws ORMException {
        Logger.infoLog("SQL Query", sqlStmt);
        try (Connection conn = getConnection()) {
            return sqlExecutor.executeUpdateStatment(conn, sqlStmt);
        } catch (SQLException | SQLExecutorException error) {
            Logger.errorLog("SQL Execution Exception", "SQL execution failed: " + error.getMessage());
            throw new ORMException("SQL execution failed: " + error.getMessage(), error);
        }
    }

    /**
     * Inserta o actualiza un registro de la entidad.
     *
     * <p>Si {@code id} es {@code null} se realiza un INSERT; si {@code id} no es {@code null}
     * se realiza un UPDATE sobre el registro identificado por {@code id}.</p>
     *
     * @param entity entidad con los datos a insertar/actualizar (no puede ser {@code null})
     * @param id     identificador del registro a actualizar (si es {@code null} se inserta)
     * @return número de filas afectadas
     * @throws ORMException si falla la creación de la sentencia o la ejecución
     */
    @Override
    public int updateQuery(T entity, String id) throws ORMException
    {
        try {
            // Se crea el string que almacena el sql statment de la cosnulta
            String sqlStmt = null;

            // Si la id es null indica que es una insert de un nuevo registro
            if (id == null && entity != null) {
                sqlStmt = sqlBuilder.createInsertQuery(entity);
            } else if (id != null && entity != null) {
                sqlStmt = sqlBuilder.createUpdateQuery(entity, id);
            } else {
                // Si no es un caso de insert o update se lanza una excepción
                throw new ORMException("Invalid Values Provided");
            }

            return executeUpdate(entity, sqlStmt);

        } catch (ORMException | EntityMapperException error) {
            Logger.errorLog("ORM Exception", "UPDATE query failed: " + error.getMessage());
            throw new ORMException("Failed to execute UPDATE query: " + error.getMessage(), error);
        }
    }
    /**
     * Elimina un registro de la entidad por su identificador.
     *
     * @param entity entidad a la que pertenece el registro (no puede ser {@code null})
     * @param id     identificador del registro a eliminar (no puede ser {@code null})
     * @return número de filas afectadas
     * @throws ORMException si faltan parámetros o falla la ejecución del DELETE
     */
    @Override
    public int deleteQuery(T entity, String id) throws ORMException
    {
        try {
            // Si no se proporciono ningun parametro se lanza una excepción
            if (entity == null || id == null){
                throw new ORMException("Entity or ID cannot be null.");
            }

            // Se crea el sql statment de un delete
            String sqlStmt = sqlBuilder.createDeleteQuery(entity, id);

            return executeUpdate(entity, sqlStmt);

        } catch (ORMException error) {
            Logger.errorLog("ORM Exception", "DELETE query failed: " + error.getMessage());
            throw new ORMException("Failed to execute DELETE query: " + error.getMessage(), error);
        }
    }

}