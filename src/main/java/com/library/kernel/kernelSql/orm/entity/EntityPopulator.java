package com.library.kernel.kernelSql.orm.entity;

import com.library.kernel.classes.Colors;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.entity.utils.EntityPopulatorException;

import java.lang.reflect.Field;

public final class EntityPopulator<T extends Entity> implements Colors
{
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
