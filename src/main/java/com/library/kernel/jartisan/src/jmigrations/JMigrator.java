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

public final class JMigrator {

    public static<T extends Migration> boolean startMigrations() throws ConnectionManagerException, JMigratorReaderException, ClassNotFoundException, JMigratorFactoryException {

//KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);

        ArrayList<String> cla = JMigratorReader.readMigrations();
        ArrayList<T> A = new ArrayList<>();

        for (String string : cla) {
            A.add((T) JMigratorFactory.createEntity(Class.forName("com.library.database.migrations." + string.toString())));
        }
        ArrayList<Schema> ad = new ArrayList<Schema>();
        for (int i = 0; i < A.size()-1; i++) {
            ad = A.get(i).up();
            System.out.println(A.get(i).toString());
            for (int j = 0; j < ad.size()-1; j++) {
                ad.get(j).toString();
            }
        }

        //System.out.println(ConnectionManagar.getConnection().toString());

        return true;

    }

    public static void main(String[] args) throws ConnectionManagerException, InterruptedException, IOException, JMigratorReaderException, JMigratorFactoryException, ClassNotFoundException {
        JMigrator.startMigrations();


    }

}
