package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.logger.Logger;

import java.lang.reflect.InvocationTargetException;

final class JMigratorFactory implements Colors {

    // Método para crear una entity de tipo T
    public static<T extends Migration> T createEntity(String migrationClass)
            throws JMigratorFactoryException
    {
        try {
            T newMigration = (T) Class.forName("com.library.database.migrations." + migrationClass).getDeclaredConstructor().newInstance();
            return newMigration;

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException |
                 IllegalAccessException | IllegalArgumentException | ClassNotFoundException error) {
            Logger.errorLog("Jartisan Migrator Factory Exception", error.getMessage());
            throw new JMigratorFactoryException("Jartisan Migrator Factory Exception: " + error.getMessage());
        }
    }
}