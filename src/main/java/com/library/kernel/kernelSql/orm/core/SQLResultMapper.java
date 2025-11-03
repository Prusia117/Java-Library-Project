package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.classes.Colors;
import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.entity.EntityFactory;
import com.library.kernel.kernelSql.orm.entity.EntityMapper;
import com.library.kernel.kernelSql.orm.entity.EntityPopulator;
import com.library.kernel.logger.Logger;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class SQLResultMapper implements Colors {

    private static<T extends Entity> void populateRelationships(ResultSet resultSet, Field[] entityFields , T entity, int j){
        try {
            for (int i = 0; i < entityFields.length - 1; i++) {
                j++;
                Logger.infoLog(
                        YELLOW + "Entity Info",
                        "Entity Field => " + entityFields[i].getName() + RESET + " | " + PURPLE
                                + "Entity Value => " + resultSet.getObject(i + 1)
                );
                // Se setea los valores de las filas en el objeto
                EntityPopulator.populateEntity(entity, entityFields[i], resultSet.getObject(j));
            }
        }catch(Exception error){

        }
    }

    public static<T extends Entity> ArrayList<T> mapResultSetToEntities(ResultSet resultSet, T entity) throws Exception{
        try {
            ArrayList<T> entityArray = new ArrayList<T>();

            Field[] entityFields = EntityMapper.mapEntityAttributes(entity.getClass());
            while (resultSet.next()) {
                T newEntity = EntityFactory.createEntity(entity.getClass());

                // Se obtienen las entities relacionadas con la entity actual
                HashMap<Class<?>, String> relationshipsEntities = newEntity.getRelationships();

                // Se crea un arraylist que almacenera las entities relacionadas con la entity actual
                ArrayList<Entity> relationshipEntities = new ArrayList<Entity>();

                populateRelationships(resultSet, entityFields, newEntity, 1);

                // Se comprueba si la tabla tiene relaciones
                if (relationshipsEntities != null) {

                    /*
                     * Se repite el procedimiento anterior se obtiene una clase luego se obtienen
                     * los atributos de la entity se crea una nueva entity y se setea los valores
                     * de estas con los datos de la base de datos
                     */
                    for (Map.Entry<Class<?>, String> relationship : relationshipsEntities.entrySet()) {

                        Class<?> newClassEntity = relationship.getKey();

                        Field[] relationshipEntityFields = EntityMapper.mapEntityAttributes(newClassEntity);

                        T newRelationshipEntity = EntityFactory.createEntity(newClassEntity);

                        populateRelationships(resultSet, relationshipEntityFields, newRelationshipEntity, relationshipEntityFields.length);

                        // Se gaurda la entity relacionada con la nueva entity actual en el array anteriormente creado
                        relationshipEntities.add(newRelationshipEntity);
                    }
                    // Se agregan las entities relacionadas con la nueva entity
                    newEntity.setEntityRelationships(relationshipEntities);
                }
                // Se agrega la nueva entity al array de nuevas entities
                entityArray.add(newEntity);

            }

            return entityArray;
        }catch(Exception error){
            throw new Exception("a");
        }
    }

}
