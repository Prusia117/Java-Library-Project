package com.library.kernel.kernelSql.orm.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.entity.utils.EntityMapperException;

public final class EntityMapper<T extends Entity> implements Colors {
    
    // Método para mapear una entity que se le pasa un objeto de tipo T
    public static<T extends Entity> HashMap<String, String> mapEntityFieldsAndAttributes(T entity)
    throws EntityMapperException
    {
        try {
            // Se crea un LinkedHashMap para preservar el orden de las atributos de la entity
            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            // Se obtiene la clase de la entity de la que le pasamos
            Class<?> entityClass = entity.getClass();
        
            // Obtenemos los atributos de la clase consulta para mas información el archivo en examples/GetFieldNames
            Field[] entityFields = entityClass.getFields();
            
            // Se mapea la entity y se obtienen los nombres de los atributos y sus valores
            for (Field field : entityFields) {
                map.put(field.getName(), String.valueOf(field.get(entity)));
            }
            
            return map;

        } catch (IllegalArgumentException | IllegalAccessException error) {
            Logger.errorLog("Entity Factory Error", error.getMessage());
            throw new EntityMapperException("Entity Factory Error: " + error.getMessage());
        }
    }

    // Método para mapear los atributos una entity que se le pasa un objeto de tipo T
    public static<T extends Entity> Field[] mapEntityAttributes(Class<?> entityClass)
    throws EntityMapperException
    {
        try {

            // Obtenemos los atributos de la clase consulta para mas información el archivo en examples/GetFieldNames
            Field[] entityFields = entityClass.getFields();
            
            return entityFields;

        } catch (IllegalArgumentException error) {
            Logger.errorLog("Entity Factory Error", error.getMessage());
            throw new EntityMapperException("Entity Factory Error: " + error.getMessage());
        }
    }
}
