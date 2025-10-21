package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.library.kernel.classes.Colors;

/*
 * Se declara un generic T que extiende de Entity es decir:
 * Es un tipo desconosido pero que tiene que se una implentación
 * o una herencia de Entity
 */
public final class EntityFactory<T extends Entity> implements Colors {

    // Método para mapear una entity que se le pasa un objeto de tipo T
    public static<T extends Entity> HashMap<String, String> entityMapper(T entity) 
    throws IllegalArgumentException, IllegalAccessException
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

        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Entity Factory Error: " + e);
            return null;

        }
    }

    // Método para crear una entity de tipo T
    public static<T extends Entity> T createEntity(Class<?> entityClass) 
    throws InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException
    {
        try {
            // Se crea una entity
            T newEnity = (T) entityClass.getDeclaredConstructor().newInstance();

            return newEnity;

        } catch ( IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {

            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Entity Factory Error: " + e);

            return null;

        }
    }
}
