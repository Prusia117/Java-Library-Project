package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.logger.Logger;
import com.library.database.migrations.*;

import java.lang.reflect.InvocationTargetException;

public final class JMigratorFactory implements Colors {

    // Método para crear una entity de tipo T
    public static Migration createEntity(Class<?> migrationClass)
            throws JMigratorFactoryException
    {
        try {
            // Se crea una entity
            Migration newMigration = (Migration) migrationClass.getDeclaredConstructor().newInstance();
            System.out.println(GREEN + "New Migration Created: " + newMigration.up() + RESET);
            return newMigration;

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 SecurityException | IllegalAccessException | IllegalArgumentException error) {
            Logger.errorLog("Entity Populator Exception", error.getMessage());
            throw new JMigratorFactoryException("Entity Populator Exception: " + error.getMessage());
        }
    }
}
