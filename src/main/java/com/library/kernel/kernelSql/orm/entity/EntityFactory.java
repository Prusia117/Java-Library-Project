package com.library.kernel.kernelSql.orm.entity;

import java.lang.reflect.InvocationTargetException;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.entity.utils.EntityFactoryException;

/**
 * Fábrica de entidades {@code Entity}.
 *
 * Proporciona un método estático para crear instancias de entidades mediante
 * reflexión. La clase no mantiene estado y solo expone utilidades para instanciar
 * objetos que implementan la interfaz {@code Entity}.
 *
 * Nota: la creación se realiza usando el constructor sin argumentos de la clase
 * proporcionada; si no existe o no es accesible, se lanzará {@link EntityFactoryException}.
 */
public final class EntityFactory<T extends Entity> implements Colors {

    /**
     * Crea una nueva instancia de la clase de entidad indicada.
     *
     * @param entityClass Clase concreta que implementa {@code Entity}.
     * @param <T>         Tipo genérico que extiende {@code Entity}.
     * @return Nueva instancia de la entidad creada mediante su constructor por defecto.
     * @throws EntityFactoryException Si ocurre cualquier error durante la instanciación
     *                                (no existe constructor por defecto, problemas de acceso, etc.).
     */
    public static<T extends Entity> T createEntity(Class<?> entityClass) 
    throws EntityFactoryException
    {
        try {
            // Se crea una instancia de la entidad usando el constructor sin argumentos
            T newEnity = (T) entityClass.getDeclaredConstructor().newInstance();

            return newEnity;

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | 
                SecurityException | IllegalAccessException | IllegalArgumentException error) {
            Logger.errorLog("Entity Populator Exception", error.getMessage());
            throw new EntityFactoryException("Entity Populator Exception: " + error.getMessage());
        }
    }
}
