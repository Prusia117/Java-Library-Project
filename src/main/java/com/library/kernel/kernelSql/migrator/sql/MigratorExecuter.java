package com.library.kernel.kernelSql.migrator.sql;

import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.sql.utils.MigratorExecutorException;
import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MigratorExecuter {

    private static Connection getConnection() throws MigratorExecutorException {
        try {
            return ConnectionManagar.getConnection();
        } catch (ConnectionManagerException error) {
            Logger.errorLog("Migrator Executor Exception", error.getMessage());
            throw new MigratorExecutorException("Migrator Executor Exception" + error.getMessage());
        }
    }

    public static void execute(ArrayList<Schema> sqlDDLStatements) throws MigratorExecutorException
    {
        try {
            for (int i = 0; i < sqlDDLStatements.size() - 1; i++) {
                String sqlStmt = sqlDDLStatements.get(i).toString();
                try (
                        Connection connection = getConnection();
                        Statement stmt = connection.createStatement();) {

                    // Se ejecuta el sql statment
                    int result = stmt.executeUpdate(sqlStmt);

                    if (result == 0) {
                        throw new MigratorExecutorException("Falied ");
                    }

                    Logger.infoLog("SQL Query", sqlStmt);

                } catch (SQLException | MigratorExecutorException error) {
                    Logger.errorLog("Migrator Executor SQL Exception", error.getMessage());
                    throw new MigratorExecutorException("Migrator Executor SQL Exception: " + error.getMessage());
                }
            }
        } catch (MigratorExecutorException error) {
            Logger.errorLog("Migrator Executor Exception", error.getMessage());
            throw new MigratorExecutorException("Migrator Executor Exception: " + error.getMessage());
        }


    }

}
