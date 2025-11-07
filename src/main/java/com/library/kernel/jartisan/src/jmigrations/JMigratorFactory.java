package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorFactoryException;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.logger.Logger;

import java.lang.reflect.InvocationTargetException;

final class JMigratorFactory implements Colors {

    /**
     * Crea una nueva instancia de la migración indicada por su nombre de clase.
     *
     * @param <T> tipo concreto que extiende {@link Migration}
     * @param migrationClass nombre simple de la clase de migración (sin el package)
     * @return nueva instancia de la migración solicitada
     * @throws JMigratorFactoryException si ocurre cualquier error durante la carga o instanciación
     */
    public static<T extends Migration> T createEntity(String migrationClass)
            throws JMigratorFactoryException
    {
        try {
            // Construir el nombre completo de la clase y crear una nueva instancia
            T newMigration = (T) Class.forName("com.library.database.migrations." + migrationClass).getDeclaredConstructor().newInstance();
            return newMigration;

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException |
                 IllegalAccessException | IllegalArgumentException | ClassNotFoundException error) {
            Logger.errorLog("Jartisan Migrator Factory Exception", error.getMessage());
            throw new JMigratorFactoryException("Jartisan Migrator Factory Exception: " + error.getMessage());
        }
    }
}