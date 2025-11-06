package com.library.kernel.kernelSql.orm.entity;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.entity.utils.EntityPopulatorException;

import java.lang.reflect.Field;
/**
 * Clase utilitaria para poblar campos de una entidad mediante reflexión.
 *
 * Esta clase final expone un único método estático que asigna un valor a un
 * campo (java.lang.reflect.Field) de una instancia de {@code Entity}.
 * La lógica se centra en usar reflexión para establecer el valor y manejar
 * la excepción {@link IllegalAccessException} envolviéndola en
 * {@link EntityPopulatorException}.
 *
 * @param <T> Tipo concreto que extiende {@code Entity}
 */
public final class EntityPopulator<T extends Entity> implements Colors
{
    /**
     * Asigna el *attributeValue* al *entityField* de la *entity* proporcionada.
     *
     * Este método usa {@link Field#set(Object, Object)} para establecer el valor.
     * Si el campo no es accesible se captura {@link IllegalAccessException},
     * se registra en el log y se lanza una {@link EntityPopulatorException}.
     *
     * @param entity Instancia de la entidad cuyo campo se desea poblar.
     * @param entityField Campo reflejado que será asignado.
     * @param attributeValue Valor que se asignará al campo.
     * @throws EntityPopulatorException Si ocurre un error de acceso/reflexión.
     */
    public static<T extends Entity> void populateEntity(T entity, Field entityField, Object attributeValue)
    throws EntityPopulatorException
    {
        try {
            entityField.set(entity, attributeValue);
        } catch (IllegalAccessException error) {
            Logger.errorLog("Entity Populator Exception", error.getMessage());
            throw new EntityPopulatorException("Entity Populator Exception: " + error.getMessage());
        }
    }
}
