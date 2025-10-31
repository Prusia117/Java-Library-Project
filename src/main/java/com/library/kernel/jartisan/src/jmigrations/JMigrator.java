package com.library.kernel.jartisan.src.jmigrations;

import com.library.config.DatabaseConfig;
import com.library.kernel.KernelConfiguration;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorReaderException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorSQLExecutorException;
import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public final class JMigrator {

    public static final List<String> J_SUPPORTED_MIGRATIONS_ACTIONS = Arrays.asList("Execute", "Rollback");


    private static<T extends Migration> boolean runMigrations() throws JMigratorException {
        try {

            KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);

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

            KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);

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

    public static boolean runMigrationsCommand(String migrationAction) throws JMigratorException {
        if(migrationAction.equals("Execute")){
            return runMigrations();
        }else if(migrationAction.equals("Rollback")){
            return rollbackMigrations();
        }
        throw new JMigratorException("Invalid Migration Action: " + migrationAction);
    }

}
