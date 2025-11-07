package com.library.kernel.jartisan.src.jmigrations;

import com.library.config.DatabaseConfig;
import com.library.kernel.KernelConfiguration;
import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorReaderException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorSQLExecutorException;
import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Clase utilitaria para ejecutar o revertir migraciones desde la línea de comandos.
 *
 * <p>Esta clase inicializa la configuración del kernel, lee las clases de migración,
 * crea instancias de migraciones mediante la factoría y delega la ejecución o
 * rollback al ejecutor SQL.</p>
 */
public final class JMigrator implements Colors {
    /**
     * Acciones soportadas por el migrador.
     * Valor permitido: "Execute" o "Rollback".
     */
    public static final List<String> J_SUPPORTED_MIGRATIONS_ACTIONS = Arrays.asList("Execute", "Rollback");
    /**
     * Inicializa la configuración global del kernel utilizando la implementación
     * de {@code DatabaseConfig}. Llamado internamente antes de ejecutar migraciones.
     */
    private static void startKernel(){
        KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);
    }


    /**
     * Lee, instancia y ejecuta las migraciones en orden.
     *
     * @param <T> tipo concreto que extiende {@link Migration}
     * @return {@code true} si las migraciones se ejecutaron correctamente, {@code false} en caso contrario
     * @throws JMigratorException si ocurre cualquier error al leer, crear o ejecutar migraciones
     */
    private static<T extends Migration> boolean runMigrations() throws JMigratorException {
        try {
            // Inicializar kernel y leer clases de migración disponibles
            startKernel();

            ArrayList<String> migrationClasses = JMigratorReader.readMigrations();
            LinkedHashMap<String, T> migrationMap = new LinkedHashMap<String, T>();

            // Instanciar cada migración mediante la factoría
            for (String migrationClass : migrationClasses) {
                migrationMap.put(migrationClass, JMigratorFactory.createEntity(migrationClass));
            }
            // Ejecutar migraciones delegando en el ejecutor SQL
            return JMigratorSQLExecutor.runMigrations(ConnectionManagar.getConnection(), migrationMap);


        }catch (JMigratorSQLExecutorException | JMigratorFactoryException | JMigratorReaderException | ConnectionManagerException error){
            throw new JMigratorException("Error Running Migrations: " + error.getMessage());
        }

    }

    /**
     * Lee, instancia y revierte las migraciones en orden inverso.
     *
     * @param <T> tipo concreto que extiende {@link Migration}
     * @return {@code true} si el rollback se realizó correctamente, {@code false} en caso contrario
     * @throws JMigratorException si ocurre cualquier error al leer, crear o revertir migraciones
     */
    private static<T extends Migration> boolean rollbackMigrations() throws JMigratorException {
        try {
            // Inicializar kernel y leer clases de migración disponibles
            startKernel();

            ArrayList<String> migrationClasses = JMigratorReader.readMigrations();
            LinkedHashMap<String, T> migrationMap = new LinkedHashMap<String, T>();

            // Instanciar cada migración mediante la factoría
            for (String migrationClass : migrationClasses) {
                migrationMap.put(migrationClass, JMigratorFactory.createEntity(migrationClass));
            }
            // Ejecutar rollback delegando en el ejecutor SQL
            return JMigratorSQLExecutor.rollbackMigrations(ConnectionManagar.getConnection(), migrationMap);


        }catch (JMigratorSQLExecutorException | JMigratorFactoryException | JMigratorReaderException | ConnectionManagerException error){
            throw new JMigratorException("Error Running Migrations: " + error.getMessage());
        }

    }
    /**
     * Punto de entrada para ejecutar el comando de migraciones.
     *
     * <p>Valida la acción solicitada ({@code Execute} o {@code Rollback}), ejecuta
     * la operación correspondiente e informa el resultado mediante el logger.</p>
     *
     * @param migrationAction acción a realizar: "Execute" o "Rollback"
     * @throws JMigratorException si la acción es inválida o si ocurre un error durante la operación
     */
    public static void runMigrationsCommand(String migrationAction) throws JMigratorException {
        boolean result = false;
        if(migrationAction.equals("Execute")){
            result = runMigrations();
        }else if(migrationAction.equals("Rollback")){
            result = rollbackMigrations();
        }
        if(result){
            Logger.infoLog("Jartisan Migrator", "Migration " + migrationAction.toLowerCase() + GREEN + " successfully" + RESET);
            return;
        }else{
            Logger.errorLog("Jartisan Migrator Exception", "Migration " + migrationAction.toLowerCase() + RED + " failed" + RESET);
        }

        throw new JMigratorException("Invalid Migration Action: " + migrationAction);
    }

}
