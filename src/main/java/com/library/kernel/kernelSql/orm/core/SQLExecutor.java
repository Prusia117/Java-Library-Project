package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

final class SQLExecutor {

    public static int excuteSelectQuery(Connection connection, String sqlStmt) throws SQLExecutorException {
        try (
                Statement stmt = connection.createStatement();) {

            // Se ejecuta el sql statment
            int result = stmt.executeUpdate(sqlStmt);

            Logger.infoLog("SQL Query", sqlStmt);
            return result;

        } catch (SQLException error) {
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }

    public static int executeUpdateStatment(Connection connection, String sqlStmt) throws SQLExecutorException {
        try (
                Statement stmt = connection.createStatement();) {

            // Se ejecuta el sql statment
            int result = stmt.executeUpdate(sqlStmt);

            Logger.infoLog("SQL Query", sqlStmt);
            return result;

        } catch (SQLException error) {
            Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
            throw new SQLExecutorException("ORM SQL Error Query Exception: " + error.getMessage());
        }
    }

}
