package com.library.kernel.jartisan.src.jmigrations;

import com.library.config.DatabaseConfig;
import com.library.kernel.KernelConfiguration;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorReaderException;
import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.utils.ConnectionManagerException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class JMigrator {

    public static<T extends Migration> boolean runMigrations() throws ConnectionManagerException, JMigratorReaderException, ClassNotFoundException, JMigratorFactoryException {


        ArrayList<String> migrationClasses = JMigratorReader.readMigrations();
        ArrayList<T> migrationInstances = new ArrayList<T>();
        ArrayList<Schema> schemasArray = null;
        LinkedHashMap<String, T> migrationMap = new LinkedHashMap<String, T>();

        for (String migrationClass : migrationClasses) {
            migrationMap.put(migrationClass, JMigratorFactory.createEntity(Class.forName("com.library.database.migrations." + migrationClass)));
        }



        for (int i = 0; i < migrationInstances.size(); i++) {
            schemasArray = migrationInstances.get(i).up();
            for (int j = 0; j < schemasArray.size(); j++) {
                System.out.println(schemasArray.get(j).toString());
            }
        }

        //System.out.println(ConnectionManagar.getConnection().toString());

        return true;

    }

    public static void main(String[] args) throws ConnectionManagerException, InterruptedException, IOException, JMigratorReaderException, JMigratorFactoryException, ClassNotFoundException {
        JMigrator.runMigrations();


    }

}
