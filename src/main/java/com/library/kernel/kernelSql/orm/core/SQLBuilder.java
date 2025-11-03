package com.library.kernel.kernelSql.orm.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.core.utils.SQLBuilderException;
import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.entity.EntityFactory;
import com.library.kernel.kernelSql.orm.entity.EntityMapper;

import com.library.kernel.classes.*;
import com.library.kernel.kernelSql.orm.entity.utils.EntityFactoryException;;

final class SQLBuilder implements ISQLBuilder ,Colors {

    @Override
    public String[] createJoinsStatments(HashMap<Class<?>, String> relationships)
    throws SQLBuilderException
    {
        try{

            if(relationships == null || relationships.size() < 1){
                throw new SQLBuilderException("Invalid SQL Relationships Provided");
            }

            // Se crea el string que almacena los joins de la tabla es StringBuilder para preservar la dirección de memoria
            StringBuilder joins = new StringBuilder();

            // Se crea el string que almacena las columnas de las tablas externas es StringBuilder para preservar la dirección de memoria
            StringBuilder joinsSqlColumns = new StringBuilder();

            /*
             * Se obtiene un hashmap con las relaciones de la tabla:
             * Class<?> representa a la entity externa
             * String representa el campo por el cual la tabla dependiente se relaciona
             * con la tabla independiente
             */

            // Se recorre el hashmap con un foreach
            for (Map.Entry<Class<?>, String> relationship : relationships.entrySet()) {

                // Se crea una nueva entity de tipo Class<?> consulta la clase par mas información
                Entity newRelationalEntity = EntityFactory.createEntity(relationship.getKey());

                // Se agrega los joins de la tabla
                joins.append(" INNER JOIN ")
                    .append(newRelationalEntity.getTableName())
                    .append(" ON ")
                    .append(newRelationalEntity.getPrimaryKey())
                    .append(" = ")
                    .append(relationship.getValue()); // Representa el atributo que une a Tabla A con Tabla B

                // Se mapea la nueva entity consulta la clase par mas información
                Field[] newEntityAttributes  = EntityMapper.mapEntityAttributes(newRelationalEntity.getClass());

                // Se recorre el hashmap con un foreach
                for (Field newEntityAttribute : newEntityAttributes) {
                    // Se omite el campo entityRelationships ya que contiene las relaciones de las
                    // tablas
                    if (newEntityAttribute.getName().equals("entityRelationships")) {
                        joinsSqlColumns.append(newEntityAttribute.getName() + ", ");
                    }
                }
            }

            String[] JoinsStatments = new String[] {joins.toString(), joinsSqlColumns.substring(0, joinsSqlColumns.length() - 2).toString()};

            return JoinsStatments;
        }catch(SQLBuilderException | EntityFactoryException error){

            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    @Override
    public String createSelectQuery(Entity entity, String value, String field)
            throws SQLBuilderException {
        try {
            if(entity == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            // Se crea el string que almacena el sql statment de la cosnulta es StringBuilder para preservar la dirección de memoria
            StringBuilder sqlStmt = new StringBuilder();

            // Se crea el string que almacena las columnas de la tabla es StringBuilder para preservar la dirección de memoria
            StringBuilder sqlColumns = new StringBuilder();

            String[] joinsStatments = null;

            // Se mapea los atributos de la entidad consulta la clase EntityMapper par mas información
            Field[] entityAttributes = EntityMapper.mapEntityAttributes(entity.getClass());

            for (Field entityAttribute : entityAttributes) {
                // Se omite el campo entityRelationships ya que contiene las relaciones de las tablas
                if (!entityAttribute.getName().contentEquals("entityRelationships")) {
                    sqlColumns.append(entityAttribute.getName() + ", ");
                }
            }

            // Se comprueba si la tabla tiene relaciones
            if (entity.getRelationships() != null) {
                joinsStatments = createJoinsStatments(entity.getRelationships());
            }

            sqlStmt.append("SELECT ");

            // Se comprueba si la tabla tiene relaciones
            if (entity.getRelationships() != null) {
                // agrega los joins correspondientes de la tabla
                sqlStmt.append(sqlColumns);
                sqlStmt.append(joinsStatments[1]);
            } else {
                // agrega un select con solo las columnas de la tabla dependiente
                sqlStmt.append(sqlColumns.substring(0, sqlColumns.length() - 2));
            }
            sqlStmt.append(" FROM ")
                    .append(entity.getTableName());

            // Si tiene relaciones se agregan al sql statment de la cosnulta
            if (entity.getRelationships() != null) {
                sqlStmt.append(joinsStatments[0]);
            }

            if (value != null) {
                sqlStmt.append(" WHERE ")
                        .append(
                                field != null ? field : entity.getPrimaryKey()
                        )
                        .append(" = ")
                        .append(value);
            }

            // Si agrega un order by por el id de tipo ASC
            sqlStmt.append(" ORDER BY ")
                    .append(entity.getPrimaryKey())
                    .append(" ASC;");
        
        return sqlStmt.toString();

        } catch (SQLBuilderException error) {
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    @Override
    public String createInsertQuery(Entity entity)
    throws SQLBuilderException
    {
        try {
            StringBuilder sqlStmt = new StringBuilder();

            // Se crea la base del sql Statmnet con la palabra insert y el nombre de la tabla
            sqlStmt.append("INSERT INTO " + entity.getTableName() + " (");

            //Se mapea la entity consulta la clase EntityFactory para mayor información
            HashMap<String, String> map = EntityMapper.mapEntityFieldsAndAttributes(entity);

            // Se crean los strings que almacenaran los atributos y los valores de estas
            StringBuilder sqlColumns = new StringBuilder();
            StringBuilder sqlValues = new StringBuilder();

            // Se recorre el map de la entity para asignarle los valores a sqlColumns y sqlValues
            for (Map.Entry<String, String> entry : map.entrySet()) {

                // Se hace un continue si la entity tiene autoincrementable y se omite el campo entityRelationships
                if ((entity.getAutoIncrement() && entry.getKey() == entity.getPrimaryKey())
                        || entry.getKey() == "entityRelationships") {
                    continue;
                }
                sqlColumns.append(entry.getKey() + ", ");
                sqlValues.append("'" + entry.getValue() + "'" + ", ");
            }

            // Se completa el sql statment
            sqlStmt.append(
                    sqlColumns.substring(0, sqlColumns.length() - 2)
                            + ") VALUES ("
                            + sqlValues.substring(0, sqlValues.length() - 2) + ");");

            return sqlStmt.toString();
        }catch (SQLBuilderException error){
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    @Override
    public String createUpdateQuery(Entity entity, String id)
    throws SQLBuilderException
    {
        try {

            if(entity == null || id == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            StringBuilder sqlStmt = new StringBuilder();

                // Se crea la base del sql Statmnet con la palabra update, el nombre de la tabla y la palabra set
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                //Se mapea la entity consulta la clase EntityFactory para mayor información
                HashMap<String, String> map = EntityMapper.mapEntityFieldsAndAttributes(entity);

                // Se crean el string que almacenara los atributos y los valores de la entity
                StringBuilder sqlColumnsAndValues = new StringBuilder();

                // Se recorre el map de la entity para asignarle los valores a sqlColumnsAndValues
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    // Si el valor es null significa que no es campo a actualizar de la entity
                    if (!entry.getValue().equals("null")) {
                        Logger.infoLog("Entity Field Value", entry.getKey() + " => " + entry.getValue());
                        sqlColumnsAndValues.append(entry.getKey() + " = '" + entry.getValue() + "', ");
                    }
                }
                // Se completa el sql statment
                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            return sqlStmt.toString();
        }catch (SQLBuilderException error){

            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    @Override
    public String createDeleteQuery(Entity entity, String id) throws SQLBuilderException
    {
        try {
            if (entity == null || id == null){
                throw new SQLBuilderException("Invalid values provided");
            }
            return new String("DELETE FROM " + entity.getTableName() + " WHERE " + entity.getPrimaryKey() + " = '" + id + "';");
        }catch (SQLBuilderException error){
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }
}
