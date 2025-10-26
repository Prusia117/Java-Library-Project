package com.library.kernel.orm.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.orm.entity.utils.EntityFactoryException;
import com.library.kernel.orm.entity.utils.EntityPopulatorException;

/*
 * Se declara un generic T que extiende de Entity es decir:
 * Es un tipo desconosido pero que tiene que se una implentación
 * o una herencia de Entity
 */
public final class EntityFactory<T extends Entity> implements Colors {

    // Método para crear una entity de tipo T
    public static<T extends Entity> T createEntity(Class<?> entityClass) 
    throws EntityFactoryException
    {
        try {
            // Se crea una entity
            T newEnity = (T) entityClass.getDeclaredConstructor().newInstance();

            return newEnity;

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | 
                SecurityException | IllegalAccessException | IllegalArgumentException error) {
            Logger.errorLog("Entity Populator Exception", error.getMessage());
            throw new EntityFactoryException("Entity Populator Exception: " + error.getMessage());
        }
    }
}
