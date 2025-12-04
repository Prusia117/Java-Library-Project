package com.library.kernel.kernelSql.orm.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.entity.utils.EntityMapperException;
/**
 * Utilidad para mapear atributos públicos de una {@code Entity}.
 *
 * Esta clase final expone métodos estáticos que utilizan reflexión para:
 * - Obtener un arreglo de campos públicos de una clase de entidad.
 * - Construir un mapa (preservando el orden de declaración) con nombre de campo
 *   y valor (como cadena) a partir de una instancia de entidad.
 *
 * Nota: se usan campos públicos mediante {@link Class#getFields()}; si las
 * entidades usan campos privados, deben hacerse accesibles o adaptar la implementación.
 *
 * @param <T> Tipo que extiende {@code Entity}
 */
public final class EntityMapper<T extends Entity> implements Colors {

    /**
     * Mapea los campos públicos de la entidad a un {@link HashMap} con el nombre
     * del campo como clave y su valor (convertido a {@link String}) como valor.
     *
     * Se utiliza {@link LinkedHashMap} para preservar el orden de los atributos
     * tal como aparecen en la clase.
     *
     * @param entity Instancia de la entidad a mapear.
     * @param <T>    Tipo concreto que extiende {@code Entity}.
     * @return Mapa con pares (nombreCampo -> valorComoCadena).
     * @throws EntityMapperException Si ocurre un error de reflexión al acceder a los campos.
     */
    public static<T extends Entity> HashMap<String, String> mapEntityFieldsAndAttributes(T entity)
    throws EntityMapperException
    {
        try {
            // Usamos LinkedHashMap para preservar el orden de declaración de los campos
            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            // Obtenemos la clase de la entidad proporcionada
            Class<?> entityClass = entity.getClass();

            // Obtenemos los campos públicos de la clase (incluye campos heredados)
            Field[] entityFields = entityClass.getFields();

            // Iteramos los campos y guardamos nombre -> valor (convertido a String)
            for (Field field : entityFields) {
                map.put(field.getName(), String.valueOf(field.get(entity)));
            }
            
            return map;

        } catch (IllegalArgumentException | IllegalAccessException error) {
            Logger.errorLog("Entity Factory Error", error.getMessage());
            throw new EntityMapperException("Entity Factory Error: " + error.getMessage());
        }
    }

    /**
     * Obtiene los campos públicos declarados en la clase de entidad indicada.
     *
     * @param entityClass Clase de la entidad cuyo arreglo de campos se desea obtener.
     * @param <T>         Tipo concreto que extiende {@code Entity} (no utilizado directamente).
     * @return Arreglo de {@link Field} con los campos públicos de la clase.
     * @throws EntityMapperException Si ocurre un error de reflexión al obtener los campos.
     */    public static<T extends Entity> Field[] mapEntityAttributes(Class<?> entityClass)
    throws EntityMapperException
    {
        try {

            // Devolvemos los campos públicos
            Field[] entityFields = entityClass.getFields();
            ArrayList<Field> publicFields = new ArrayList<>();
            for (Field field : entityFields) {
                if(!Entity.ENTITY_KEY_WORDS.contains(field.getName())){

                    publicFields.add(field);
                }
            }

            return publicFields.toArray(new Field[entityFields.length - publicFields.size()]);

        } catch (IllegalArgumentException error) {
            Logger.errorLog("Entity Factory Error", error.getMessage());
            throw new EntityMapperException("Entity Factory Error: " + error.getMessage());
        }
    }
}
