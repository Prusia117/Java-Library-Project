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

public final class JMigrator implements Colors {

    public static final List<String> J_SUPPORTED_MIGRATIONS_ACTIONS = Arrays.asList("Execute", "Rollback");

    private static void startKernel(){
        KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);
    }


    private static<T extends Migration> boolean runMigrations() throws JMigratorException {
        try {

            startKernel();

            ArrayList<String> migrationClasses = JMigratorReader.readMigrations();
            LinkedHashMap<String, T> migrationMap = new LinkedHashMap<String, T>();

            for (String migrationClass : migrationClasses) {
                migrationMap.put(migrationClass, JMigratorFactory.createEntity(migrationClass));
            }

            return JMigratorSQLExecutor.runMigrations(ConnectionManagar.getConnection(), migrationMap);


        }catch (JMigratorSQLExecutorException | JMigratorFactoryException | JMigratorReaderException | ConnectionManagerException error){
            throw new JMigratorException("Error Running Migrations: " + error.getMessage());
        }

    }


    private static<T extends Migration> boolean rollbackMigrations() throws JMigratorException {
        try {

            startKernel();

            ArrayList<String> migrationClasses = JMigratorReader.readMigrations();
            LinkedHashMap<String, T> migrationMap = new LinkedHashMap<String, T>();

            for (String migrationClass : migrationClasses) {
                migrationMap.put(migrationClass, JMigratorFactory.createEntity(migrationClass));
            }

            return JMigratorSQLExecutor.rollbackMigrations(ConnectionManagar.getConnection(), migrationMap);


        }catch (JMigratorSQLExecutorException | JMigratorFactoryException | JMigratorReaderException | ConnectionManagerException error){
            throw new JMigratorException("Error Running Migrations: " + error.getMessage());
        }

    }

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
