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
/**
 * Mapea un {@code ResultSet} SQL a instancias de {@code Entity}.
 *
 * Esta clase proporciona utilidades estáticas para transformar las filas de un
 * {@code ResultSet} en objetos de dominio que extienden {@code Entity}. También
 * se encarga de poblar las relaciones (entities relacionadas) cuando la entidad
 * las declara.
 *
 * Nota: no abre ni cierra el {@code ResultSet} ni la conexión; el llamador debe
 * gestionar el ciclo de vida de los recursos JDBC.
 */
final class SQLResultMapper implements Colors {
    /**
     * Asigna valores del {@code ResultSet} a los campos de una entidad concreta.
     *
     * @param resultSet     Resultado de la consulta con la fila actual ya posicionada.
     * @param entityFields  Arreglo de campos de la entidad a poblar.
     * @param entity        Instancia de la entidad destino donde se setearán los valores.
     * @param j             Índice base del {@code ResultSet} desde el que empezar a leer.
     *
     * Este método recorre los campos indicados y utiliza {@code EntityPopulator}
     * para asignar los valores extraídos del {@code ResultSet}. Los índices de columnas
     * usados en los logs y en la lectura consideran el desplazamiento suministrado en
     * el parámetro {@code j}.
     */
    private static<T extends Entity> void populateEntities(ResultSet resultSet, Field[] entityFields , T entity, int j){
        try {

            for (int i = 0; i < entityFields.length - 1; i++) {
                Logger.infoLog(
                        YELLOW + "Entity Info",
                        "Entity Field => " + entityFields[i].getName() + RESET + " | " + PURPLE
                                + "Entity Value => " + resultSet.getObject(j)
                );
                // Se setea los valores de las filas en el objeto
                EntityPopulator.populateEntity(entity, entityFields[i], resultSet.getObject(j));
                j++;
            }
        }catch(Exception error){
            System.out.println(error.getMessage());
        }
    }
    /**
     * Convierte un {@code ResultSet} en una lista de entidades del tipo de la entidad
     * proporcionada.
     *
     * @param resultSet {@code ResultSet} obtenido de la ejecución de una consulta SELECT.
     * @param entity    Instancia de ejemplo cuya clase indica el tipo de entidad a crear.
     * @param <T>       Tipo de {@code Entity} a retornar.
     * @return Lista de entidades pobladas con los datos del {@code ResultSet}.
     * @throws Exception Si ocurre cualquier error durante la creación o el mapeo.
     *
     * El método:
     * - Crea una nueva instancia de la entidad para cada fila del {@code ResultSet}.
     * - Mapea los atributos de la entidad principal.
     * - Si la entidad declara relaciones, crea y popula las entidades relacionadas y
     *   las agrega a la entidad principal mediante {@code setEntityRelationships}.
     *
     * Nota: mantiene el mismo orden de columnas esperado por el mapeo actual.
     */
    public static<T extends Entity> ArrayList<T> mapResultSetToEntities(ResultSet resultSet, T entity) throws Exception{
        try {
            ArrayList<T> entityArray = new ArrayList<T>();

            Field[] entityFields = EntityMapper.mapEntityAttributes(entity.getClass());
            while (resultSet.next()) {
                T newEntity = EntityFactory.createEntity(entity.getClass());

                // Se obtienen las entities relacionadas con la entity actual
                HashMap<Class<?>, String> relationshipsEntities = newEntity.getRelationships();

                // ArrayList que almacenará las entities relacionadas
                ArrayList<Entity> relationshipEntities = new ArrayList<Entity>();

                // Se poblan los campos de la entidad principal (índice base 1)
                populateEntities(resultSet, entityFields, newEntity, 1);

                // Si la entidad tiene relaciones, se crean y pueblan las entidades relacionadas
                if (relationshipsEntities != null) {

                    /*
                     * Para cada relación:
                     * - Se obtiene la clase de la entidad relacionada.
                     * - Se mapean sus atributos.
                     * - Se crea una nueva instancia y se puebla con los valores del ResultSet.
                     * - Se añade la entidad relacionada al array de relaciones.
                     */
                    for (Map.Entry<Class<?>, String> relationship : relationshipsEntities.entrySet()) {

                        Class<?> newClassEntity = relationship.getKey();

                        Field[] relationshipEntityFields = EntityMapper.mapEntityAttributes(newClassEntity);

                        T newRelationshipEntity = EntityFactory.createEntity(newClassEntity);

                        populateEntities(resultSet, relationshipEntityFields, newRelationshipEntity, relationshipEntityFields.length);

                        // Se guarda la entidad relacionada en la lista de relaciones
                        relationshipEntities.add(newRelationshipEntity);
                    }
                    // Se asignan las entities relacionadas a la entidad principal
                    newEntity.setEntityRelationships(relationshipEntities);
                }
                // Se añade la nueva entidad al resultado final
                entityArray.add(newEntity);

            }

            return entityArray;
        }catch(Exception error){
            throw new Exception("a");
        }
    }

}
