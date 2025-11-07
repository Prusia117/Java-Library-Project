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
/**
 * Ejecutor SQL encargado de aplicar o revertir migraciones en la base de datos.
 *
 * <p>Esta clase lee las migraciones proporcionadas, comprueba la existencia de la
 * tabla de control de migraciones, ejecuta las sentencias DDL y mantiene una lista
 * local de migraciones aplicadas durante la ejecución actual para evitar duplicados.</p>
 */
final class JMigratorSQLExecutor implements Colors {
    /**
     * Lista en memoria de migraciones aplicadas durante la ejecución actual para
     * evitar reintentos dentro del mismo proceso.
     */
    private static ArrayList<String> migrationsThread = new ArrayList<String>();
    /**
     * Verifica si la tabla de control de migraciones existe en la base de datos.
     *
     * @param connection conexión SQL válida
     * @return {@code true} si la tabla de migraciones existe, {@code false} en caso contrario
     * @throws JMigratorSQLExecutorException si ocurre un error al consultar la base de datos
     */
    private static boolean verifyIfMigrationTableExits(Connection connection) throws JMigratorSQLExecutorException {
        try (
                // Crear statement y ejecutar la consulta construida por el builder
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(JMigratorSQLBuilder.buildGetMigrationTableQuery());

        ) {
            // Si hay resultado y el primer campo es true, la tabla existe
            if (resultSet.next() && resultSet.getBoolean(1)) {
                return true;
            }
            return false;
        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }
    /**
     * Obtiene la lista de migraciones ya registradas en la tabla de control.
     *
     * @param connection conexión SQL válida
     * @return lista de nombres de migraciones; puede ser {@code null} si no hay registros
     * @throws JMigratorSQLExecutorException si ocurre un error al obtener los registros
     */
    private static ArrayList<String> getMigrations(Connection connection) throws JMigratorSQLExecutorException {
        try (
                // Crear statement y ejecutar la consulta que devuelve las migraciones aplicadas
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(JMigratorSQLBuilder.buildGetMigrationsQuery());

        ) {
            ArrayList<String> migrations = new ArrayList<String>();
            while (resultSet.next()) {
                // Extraer el nombre de la migración usando el nombre de campo definido en el builder
                migrations.add(resultSet.getString(JMigratorSQLBuilder.getMigrationsFieldName()));
            }
            // Normalizar: devolver null si la lista está vacía (comportamiento original)
            if(migrations.isEmpty()){
                migrations = null;
            }
            return migrations;
        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }
    /**
     * Ejecuta una sentencia DDL para crear/actualizar el esquema de la migración y
     * registra la migración en la tabla de control si procede.
     *
     * <p>Si la ejecución falla, intenta eliminar cualquier rastro de la migración y
     * retorna {@code false}.</p>
     *
     * @param connection   conexión SQL válida
     * @param sqlStmt      sentencia SQL (DDL) a ejecutar
     * @param migrationName nombre identificador de la migración
     * @return {@code true} si la operación se completó correctamente, {@code false} si falló
     * @throws JMigratorSQLExecutorException si ocurre un error al ejecutar el statement
     */
    private static boolean createMigrations(Connection connection, String sqlStmt, String migrationName) throws JMigratorSQLExecutorException
    {
        try (
                Statement stmt = connection.createStatement();) {
            // Ejecutar la sentencia DDL de creación/alteración
            boolean create = stmt.execute(sqlStmt);
            Logger.infoLog("Jartisan Migrator", "Creating Schema: " + GREEN + sqlStmt + RESET);
            // stmt.execute devuelve true si el primer resultado es un ResultSet (no esperado para DDL)
            if(create){
                Logger.errorLog("Jartisan Migrator Executor Exception: ", "Failed to create migration: " + migrationName);
                // Intentar limpiar la migración parcialmente aplicada
                deleteMigrations(connection, migrationName);
                return false;
            }
            // Si la migración no está ya en la lista en memoria, registrar en la tabla de migraciones
            if(!migrationsThread.contains(migrationName)){
                boolean update = stmt.execute(JMigratorSQLBuilder.buildUpdateMigrationTableQuery(migrationName));
                if(update){
                    Logger.errorLog("Jartisan Migrator Executor Exception: ", "Failed to update migration: " + migrationName);
                    deleteMigrations(connection, migrationName);
                    return false;
                }
                // Marcar como aplicada en la ejecución actual
                migrationsThread.add(migrationName);
            }

            return true;

        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " +error.getMessage());
        }
    }
    /**
     * Elimina o revierte la migración indicada ejecutando la sentencia correspondiente.
     *
     * <p>Este método se utiliza para rollback cuando una creación parcial falla o
     * para ejecutar el rollback de una migración.</p>
     *
     * @param connection   conexión SQL válida
     * @param migrationName nombre identificador de la migración (o sentencia a ejecutar según builder)
     * @throws JMigratorSQLExecutorException si ocurre un error al ejecutar la eliminación
     */
    private static void deleteMigrations(Connection connection, String migrationName) throws JMigratorSQLExecutorException {
        try (
                Statement stmt = connection.createStatement();) {
            // Registrar la intención de rollback y ejecutar la sentencia de borrado generada por el builder
            Logger.infoLog("Jartisan Migrator Executor", "Rolling back migration: " + GREEN + migrationName);
            stmt.execute(JMigratorSQLBuilder.buildDropMigrationTableQuery(migrationName));

        } catch (SQLException error) {
            Logger.errorLog("Jartisan Migrator Executor Exception: ", error.getMessage());
            throw new JMigratorSQLExecutorException("Jartisan Migrator Executor Exception: " + error.getMessage());
        }
    }
    /**
     * Ejecuta las migraciones pendientes en orden.
     *
     * <p>Verifica la tabla de migraciones, omite las ya aplicadas, y aplica las nuevas
     * invocando {@code up()} de cada migración y ejecutando las sentencias DDL resultantes.</p>
     *
     * @param <T> tipo concreto que extiende {@link Migration}
     * @param connection conexión SQL válida
     * @param migrationsMap mapa ordenado de nombre de migración a instancia de {@code Migration}
     * @return {@code true} si todas las migraciones se aplicaron correctamente
     * @throws JMigratorSQLExecutorException si ocurre cualquier error durante la ejecución
     */
    public static<T extends Migration> boolean runMigrations(Connection connection, LinkedHashMap<String, T> migrationsMap)
    throws JMigratorSQLExecutorException
    {
        try{
            Logger.infoLog("Jartisan Migrator Executor", "Running migrations");
            // Si existe la tabla de migraciones, cargar las ya aplicadas y removerlas del mapa
            if(verifyIfMigrationTableExits(connection)){
                ArrayList<String> migrations = getMigrations(connection);
                if(!migrations.isEmpty()){
                    for (String migration : migrations) {
                        migrationsMap.remove(migration);
                    }
                }
                if(migrationsMap.isEmpty()){
                    Logger.infoLog("Jartisan Migrator Executor", "All Migrations have been executed. Nothing to do");
                    return true;
                }
            }

            // Iterar sobre las migraciones restantes y aplicar cada esquema
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
    /**
     * Revierte las migraciones en orden inverso al de aplicación.
     *
     * <p>Construye una lista invertida de las migraciones recibidas y ejecuta
     * {@code rollback()} de cada una, aplicando las sentencias de reversión.</p>
     *
     * @param <T> tipo concreto que extiende {@link Migration}
     * @param connection conexión SQL válida
     * @param migrationsMap mapa ordenado de nombre de migración a instancia de {@code Migration}
     * @return {@code true} si todas las migraciones fueron revertidas correctamente
     * @throws JMigratorSQLExecutorException si ocurre cualquier error durante el rollback
     */
    public static<T extends Migration> boolean rollbackMigrations(Connection connection, LinkedHashMap<String, T> migrationsMap)
            throws JMigratorSQLExecutorException
    {
        try{
            Logger.infoLog("Jartisan Migrator Executor", "Running rollback migrations");
            List<Map.Entry<String, T>> reversedMigrationsMap = new ArrayList<>(migrationsMap.entrySet());
            Collections.reverse(reversedMigrationsMap);

            // Si no existe la tabla de migraciones ya no hay nada que revertir
            if(!verifyIfMigrationTableExits(connection)){
                Logger.infoLog("Jartisan Migrator Executor", "All Migrations have been rolled back. Nothing to do");
                return true;
            }

            // Iterar en orden inverso y ejecutar los rollback definidos por cada migración
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
