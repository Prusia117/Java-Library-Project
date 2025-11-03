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

/*
 * Se declara un generic T que extiende de Entity es decir:
 * Es un tipo desconosido pero que tiene que se una implentación
 * o una herencia de Entity
 */
public final class ORM<T extends Entity> implements IORMEngine<T>, Colors {
    // Método para setear los parametros de la base de datos

    private ISQLExecutor sqlExecutor;
    private ISQLBuilder sqlBuilder;

    {
        sqlExecutor = new SQLExecutor();
        sqlBuilder = new SQLBuilder();
    }

    private static Connection getConnection() throws ORMException {
        try {
            return ConnectionManagar.getConnection();
        } catch (ConnectionManagerException error) {
            Logger.errorLog("ORMException", "ORM failed to retrieve connection: " + error.getMessage());
            throw new ORMException("ORM could not obtain a valid database connection.", error);
        }
    }



    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
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

    private int executeUpdate(T entity, String sqlStmt) throws ORMException {
        Logger.infoLog("SQL Query", sqlStmt);
        try (Connection conn = getConnection()) {
            return sqlExecutor.executeUpdateStatment(conn, sqlStmt);
        } catch (SQLException | SQLExecutorException error) {
            Logger.errorLog("SQL Execution Exception", "SQL execution failed: " + error.getMessage());
            throw new ORMException("SQL execution failed: " + error.getMessage(), error);
        }
    }

    /*
     * Método updateQuery funciona para insertar o actualizar regsitro
     * Se le pasa una entity de tipo T y opcional un id de tipo String
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
    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
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