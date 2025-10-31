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
            Logger.errorLog("Entity Populator Exception", error.getMessage());
            throw new JMigratorFactoryException("Entity Populator Exception: " + error.getMessage());
        }
    }
}
/*
*     @Override
    public ArrayList<Schema> up() {
        return new ArrayList<Schema>(List.of(
                new Schema("T_MIGRATIONS")
                        .addColumn("mig_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("mig_name", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false)

        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<String>(List.of(
                "productsB"
        ));
    }*/