package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.util.HashMap;

public final class EntityMapper<T extends Entity> {

    public static<T extends Entity> HashMap<String, String> entityMapper(T entity) 
    throws ORMException
    {
        try {
            HashMap<String, String> map = new HashMap<>();

            Class<?> entityClass = entity.getClass();
        
            Field[] entityFields = entityClass.getFields();
        
            for (Field field : entityFields)  map.put(field.getName(), (String) field.get(entity));
        
            return map;

        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO: implemtar logger

            throw e;

        }
    }
}
