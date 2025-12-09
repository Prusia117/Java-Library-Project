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
import com.library.kernel.kernelSql.orm.entity.utils.EntityFactoryException;
import static com.library.kernel.kernelSql.orm.entity.Entity.ENTITY_KEY_WORDS;

/**
 * Generador de sentencias SQL básicas para entidades.
 *
 * Esta clase construye sentencias SQL de tipo SELECT, INSERT, UPDATE y DELETE,
 * así como las cláusulas JOIN necesarias cuando una entidad tiene relaciones con
 * otras entidades. Los métodos devuelven las sentencias SQL en forma de
 * cadenas y lanzan {@link SQLBuilderException} en caso de parámetros inválidos
 * o errores durante la generación.
 *
 * Nota: no ejecuta las sentencias, solo las construye.
 */
final class SQLBuilder implements ISQLBuilder ,Colors {
    /**
     * Crea las cláusulas JOIN y una lista de columnas adicionales provenientes
     * de las entidades relacionadas.
     *
     * @param relationships Mapa donde la clave es la clase de la entidad relacionada
     *                      y el valor es el campo que une la entidad dependiente
     *                      con la independiente.
     * @return Un arreglo de dos posiciones:
     *         [0] = cadena con los JOINs (por ejemplo " INNER JOIN ... ON ...")
     *         [1] = cadena con las columnas adicionales separadas por comas
     * @throws SQLBuilderException Si el parámetro es nulo o vacío o si ocurre un error
     *                             al crear las entidades relacionadas.
     */
    public String[] createJoinsStatments(HashMap<Class<?>, String> relationships)
    throws SQLBuilderException
    {
        try{

            if(relationships == null || relationships.size() < 1){
                throw new SQLBuilderException("Invalid SQL Relationships Provided");
            }

            // StringBuilder para concatenar las sentencias JOIN
            StringBuilder joins = new StringBuilder();

            // StringBuilder para concatenar las columnas provenientes de las entidades relacionadas
            StringBuilder joinsSqlColumns = new StringBuilder();

            /*
             * El mapa 'relationships' contiene:
             * - Class<?>: clase de la entidad relacionada
             * - String: campo por el cual la tabla dependiente se relaciona con la independiente
             */

            // Recorre el mapa de relaciones
            for (Map.Entry<Class<?>, String> relationship : relationships.entrySet()) {

                // Crea una instancia de Entity para la entidad relacionada
                Entity newRelationalEntity = EntityFactory.createEntity(relationship.getKey());

                // Agrega la cláusula JOIN correspondiente
                joins.append(" INNER JOIN ")
                    .append(newRelationalEntity.getTableName())
                    .append(" ON ")
                    .append(newRelationalEntity.getPrimaryKey())
                    .append(" = ")
                    .append(relationship.getValue()); // Representa el atributo que une a Tabla A con Tabla B

                // Mapea los atributos de la nueva entidad relacionada
                Field[] newEntityAttributes  = EntityMapper.mapEntityAttributes(newRelationalEntity.getClass());

                // Recorre los atributos y añade aquellos que correspondan a las columnas a seleccionar
                for (Field newEntityAttribute : newEntityAttributes) {
                    // Aquí se añade el campo 'entityRelationships' (si existe) entre las columnas adicionales.
                    // (Nota: la lógica conserva la implementación existente; si se desea omitir este campo,
                    // habría que cambiar la condición para hacer un 'continue' cuando el nombre sea 'entityRelationships'.)
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
    /**
     * Construye una sentencia SELECT para la entidad proporcionada.
     *
     * @param entity Entidad sobre la que se construye la consulta.
     * @param value  Valor para la cláusula WHERE (puede ser nulo para omitir WHERE).
     * @param field  Campo a usar en la cláusula WHERE (si es nulo se usa la clave primaria).
     * @return Cadena con la sentencia SELECT completa terminada en punto y coma.
     * @throws SQLBuilderException Si la entidad es nula o ocurre un error durante la construcción.
     */
    @Override
    public String createSelectQuery(Entity entity, String value, String field)
            throws SQLBuilderException {
        try {
            if(entity == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            // StringBuilder para la sentencia SQL completa
            StringBuilder sqlStmt = new StringBuilder();

            // StringBuilder para las columnas de la entidad actual
            StringBuilder sqlColumns = new StringBuilder();

            String[] joinsStatments = null;

            // StringBuilder para las columnas de la entidad actual
            Field[] entityAttributes = EntityMapper.mapEntityAttributes(entity.getClass());

            // Añade las columnas, omitiendo el campo 'entityRelationships'
            for (Field entityAttribute : entityAttributes) {
                // Se omite el campo entityRelationships ya que contiene las relaciones de las tablas
                if (!ENTITY_KEY_WORDS.contains(entityAttribute.getName())) {
                    sqlColumns.append(entityAttribute.getName() + ", ");
                }
            }

            // Si la entidad tiene relaciones, obtiene los JOINs y columnas adicionales
            if (entity.getRelationships() != null) {
                joinsStatments = createJoinsStatments(entity.getRelationships());
            }

            sqlStmt.append("SELECT ");

            // Si tiene relaciones, concatena las columnas locales y las columnas extra de los JOINs
            if (entity.getRelationships() != null) {
                sqlStmt.append(sqlColumns);
                sqlStmt.append(joinsStatments[1]);
            } else {
                // Si no, añade solo las columnas de la entidad actual (elimina la última coma y espacio)
                sqlStmt.append(sqlColumns.substring(0, sqlColumns.length() - 2));
            }
            sqlStmt.append(" FROM ")
                    .append(entity.getTableName());

            // Añade los JOINs si existen
            if (entity.getRelationships() != null) {
                sqlStmt.append(joinsStatments[0]);
            }

            // Añade cláusula WHERE si se proporciona un valor
            if (value != null) {
                sqlStmt.append(" WHERE ")
                        .append(
                                field != null ? field : entity.getPrimaryKey()
                        )
                        .append(" = ")
                        .append("'" + value + "'");
            }

            // Añade ORDER BY por la clave primaria de manera ascendente
            sqlStmt.append(" ORDER BY ")
                    .append(entity.getPrimaryKey())
                    .append(" ASC;");
        
        return sqlStmt.toString();

        } catch (SQLBuilderException error) {
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    /**
     * Construye una sentencia INSERT para la entidad proporcionada.
     *
     * @param entity Entidad a insertar.
     * @return Cadena con la sentencia INSERT completa terminada en punto y coma.
     * @throws SQLBuilderException Si ocurre un error al generar la sentencia.
     */
    @Override
    public String createInsertQuery(Entity entity)
    throws SQLBuilderException
    {
        try {
            StringBuilder sqlStmt = new StringBuilder();

            // Construye la parte inicial del INSERT con el nombre de la tabla
            sqlStmt.append("INSERT INTO " + entity.getTableName() + " (");

            // Mapea los campos y sus valores de la entidad
            HashMap<String, String> map = EntityMapper.mapEntityFieldsAndAttributes(entity);

            // StringBuilder para columnas y valores
            StringBuilder sqlColumns = new StringBuilder();
            StringBuilder sqlValues = new StringBuilder();

            // Recorre el mapa y arma las listas de columnas y valores
            for (Map.Entry<String, String> entry : map.entrySet()) {

                // Si la entidad tiene autoincrementable y el campo es la PK, o si es 'entityRelationships', se omite
                if ((entity.getAutoIncrement() && entry.getKey() == entity.getPrimaryKey())
                        || ENTITY_KEY_WORDS.contains(entry.getKey())) {
                    continue;
                }
                sqlColumns.append(entry.getKey() + ", ");
                sqlValues.append("'" + entry.getValue() + "'" + ", ");
            }

            // Completa la sentencia INSERT eliminando la última coma y espacio
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
    /**
     * Construye una sentencia UPDATE para la entidad especificada y el id dado.
     *
     * @param entity Entidad con los nuevos valores.
     * @param id     Identificador (valor) de la fila a actualizar.
     * @return Cadena con la sentencia UPDATE completa terminada en punto y coma.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo o ocurre un error.
     */
    @Override
    public String createUpdateQuery(Entity entity, String id)
    throws SQLBuilderException
    {
        try {

            if(entity == null || id == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            StringBuilder sqlStmt = new StringBuilder();

                // Inicia la sentencia UPDATE con el nombre de la tabla y la cláusula SET
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                // Mapea los campos y valores de la entidad
                HashMap<String, String> map = EntityMapper.mapEntityFieldsAndAttributes(entity);

                // StringBuilder para los pares columna = valor
                StringBuilder sqlColumnsAndValues = new StringBuilder();

                // Recorre el mapa y añade solo los campos cuyo valor no sea "null"
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " => " + entry.getValue());
                    if (!entry.getValue().equals("null") &&
                            !ENTITY_KEY_WORDS.contains(entry.getKey())) {
                        Logger.infoLog("Entity Field Value", entry.getKey() + " => " + entry.getValue());
                        sqlColumnsAndValues.append(entry.getKey() + " = '" + entry.getValue() + "', ");
                    }
                }
                // Completa la sentencia eliminando la última coma y espacio, y añade la cláusula WHERE
                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            return sqlStmt.toString();
        }catch (SQLBuilderException error){

            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }
    /**
     * Construye una sentencia DELETE para la entidad y el id proporcionados.
     *
     * @param entity Entidad desde la cual se eliminará la fila.
     * @param id     Identificador (valor) de la fila a eliminar.
     * @return Cadena con la sentencia DELETE completa terminada en punto y coma.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo.
     */
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
