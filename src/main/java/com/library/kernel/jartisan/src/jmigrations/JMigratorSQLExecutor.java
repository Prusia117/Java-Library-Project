package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorSQLExecutorException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.logger.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

final class JMigratorSQLExecutor implements Colors {

    private static boolean verifyIfMigrationTableExits(Connection connection) throws JMigratorSQLExecutorException {
        try (
                // Se obtiene una conexión
                Statement stmt = connection.createStatement();
                // Se ejecuta el sql statment
                ResultSet resultSet = stmt.executeQuery(JMigratorSQLBuilder.buildGetMigrationTableQuery());

        ) {
            if (resultSet.next() && resultSet.getBoolean(1)) {
                return true;
            }
            return false;
        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }

    private static ArrayList<String> getMigrations(Connection connection) throws JMigratorSQLExecutorException {
        try (
                // Se obtiene una conexión
                Statement stmt = connection.createStatement();
                // Se ejecuta el sql statment
                ResultSet resultSet = stmt.executeQuery(JMigratorSQLBuilder.buildGetMigrationsQuery());

        ) {
            ArrayList<String> migrations = new ArrayList<String>();
            while (resultSet.next()) {
                migrations.add(resultSet.getString(JMigratorSQLBuilder.getMigrationsFieldName()));
            }
            if(migrations.isEmpty()){
                migrations = null;
            }
            return migrations;
        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }

    private static boolean createMigrations(Connection connection, String sqlStmt, String migrationName) throws JMigratorSQLExecutorException
    {
        try (
                Statement stmt = connection.createStatement();) {
            boolean create = stmt.execute(sqlStmt);
            Logger.infoLog("Jartisan Migrator", "Creating Schema: " + GREEN + sqlStmt + RESET);
            if(create){
                Logger.errorLog("Jartisan Migrator Executor Exception: ", "Failed to create migration: " + migrationName);
                deleteMigrations(connection, migrationName);
                return false;
            }

            boolean update = stmt.execute(JMigratorSQLBuilder.buildUpdateMigrationTableQuery(migrationName));
            if(update){
                Logger.errorLog("Jartisan Migrator Executor Exception: ", "Failed to update migration: " + migrationName);
                deleteMigrations(connection, migrationName);
                return false;
            }

            return true;

        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " +error.getMessage());
        }
    }

    private static void deleteMigrations(Connection connection, String migrationName) throws JMigratorSQLExecutorException {
        try (
                Statement stmt = connection.createStatement();) {
            // Se ejecuta el sqlstatment
            Logger.infoLog("Jartisan Migrator Executor", "Rolling back migration: " + GREEN + migrationName);
            stmt.execute(JMigratorSQLBuilder.buildDropMigrationTableQuery(migrationName));

        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }

    public static<T extends Migration> boolean runMigrations(Connection connection, LinkedHashMap<String, T> migrationsMap)
    throws JMigratorSQLExecutorException
    {
        try{
            Logger.infoLog("Jartisan Migrator Executor", "Running migrations");
            if(verifyIfMigrationTableExits(connection)){
                ArrayList<String> migrations = getMigrations(connection);
                if(!migrations.isEmpty()){
                    for (String migration : migrations) {
                        migrationsMap.remove(migration);
                    }
                }
                if(migrationsMap.isEmpty()){
                    Logger.infoLog("Jartisan Migrator Executor", "All Migrations have been executed. Nothing to do");
                }
            }

            for (Map.Entry<String, T> migration : migrationsMap.entrySet()) {
                ArrayList<Schema> schemasArray = migration.getValue().up();
                for (Schema schema : schemasArray) {
                    String sqlStmt = schema.toString();
                    if(!createMigrations(connection, sqlStmt, migration.getKey())){
                        throw new JMigratorSQLExecutorException("Failed to create migration: " + migration.getKey());
                    }
                }
            }

            return true;
        }catch(JMigratorSQLExecutorException error){
            Logger.errorLog("Jartisan Migrator Executor Exception", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }

    public static<T extends Migration> boolean rollbackMigrations(Connection connection, LinkedHashMap<String, T> migrationsMap)
            throws JMigratorSQLExecutorException
    {
        try{
            Logger.infoLog("Jartisan Migrator Executor", "Running rollback migrations");
            List<Map.Entry<String, T>> reversedMigrationsMap = new ArrayList<>(migrationsMap.entrySet());
            Collections.reverse(reversedMigrationsMap);

            for (Map.Entry<String, T> migration : reversedMigrationsMap) {
                ArrayList<String> schemasArray = migration.getValue().rollback();
                for (String schema : schemasArray) {
                    deleteMigrations(connection, schema);
                }
            }

            return true;
        }catch(JMigratorSQLExecutorException error){
            Logger.errorLog("Jartisan Migrator Executor Exception", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }

}
