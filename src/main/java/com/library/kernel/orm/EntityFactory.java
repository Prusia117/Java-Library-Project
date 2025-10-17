package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public final class EntityFactory<T extends Entity> {

    public static<T extends Entity> HashMap<String, String> entityMapper(T entity) 
    throws IllegalArgumentException, IllegalAccessException
    {
        try {
            HashMap<String, String> map = new HashMap<>();

            Class<?> entityClass = entity.getClass();
        
            Field[] entityFields = entityClass.getFields();
        
            for (Field field : entityFields)  map.put(field.getName(), (String) field.get(entity));
        
            return map;

        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO: implemtar logger

            return null;

        }
    }

    public static<T extends Entity> String[] getEntityFields(T entity) 
    throws IllegalArgumentException, IllegalAccessException
    {
        try {
            HashMap<String, String> map = new HashMap<>();

            Class<?> entityClass = entity.getClass();
        
            Field[] entityFields = entityClass.getFields();
        
            for (Field field : entityFields)  map.put(field.getName(), (String) field.get(entity));
        
            return map;

        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO: implemtar logger

            return null;

        }
    }

    public static<T extends Entity> T createEntity(Class<?> entityClass) 
    throws InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException
    {
        try {
        
            T newEnity = (T) entityClass.getDeclaredConstructor().newInstance();

            return newEnity;

        } catch ( IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // TODO: implemtar logger

            return null;

        }
    }
}
