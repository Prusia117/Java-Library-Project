package com.library.kernel.kernelSql.orm.core;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * otras entidades. Las sentencias se devuelven parametrizadas ({@link SQLStatement}):
 * los valores nunca se concatenan en el SQL, y los nombres de columna solo pueden
 * provenir de los campos declarados en la entidad.
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
     *               Debe ser uno de los campos declarados en la entidad.
     * @return Sentencia SELECT parametrizada.
     * @throws SQLBuilderException Si la entidad es nula, el campo no existe en la entidad
     *                             o el valor no es compatible con el tipo del campo.
     */
    @Override
    public SQLStatement createSelectQuery(Entity entity, String value, String field)
            throws SQLBuilderException {
        try {
            if(entity == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            // StringBuilder para la sentencia SQL completa
            StringBuilder sqlStmt = new StringBuilder();

            // StringBuilder para las columnas de la entidad actual
            StringBuilder sqlColumns = new StringBuilder();

            // Valores a enlazar en la sentencia preparada
            List<Object> params = new ArrayList<>();

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
                // El nombre de columna no se puede parametrizar, así que se valida contra la entidad
                Field whereField = findColumn(entityAttributes, field != null ? field : entity.getPrimaryKey());

                sqlStmt.append(" WHERE ")
                        .append(whereField.getName())
                        .append(" = ?");
                params.add(toColumnType(value, whereField));
            }

            // Añade ORDER BY por la clave primaria de manera ascendente
            sqlStmt.append(" ORDER BY ")
                    .append(entity.getPrimaryKey())
                    .append(" ASC;");

        return new SQLStatement(sqlStmt.toString(), params);

        } catch (SQLBuilderException error) {
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    /**
     * Construye una sentencia INSERT para la entidad proporcionada.
     * Los campos con valor nulo se omiten para que la base de datos aplique su valor por defecto.
     *
     * @param entity Entidad a insertar.
     * @return Sentencia INSERT parametrizada.
     * @throws SQLBuilderException Si ocurre un error al generar la sentencia.
     */
    @Override
    public SQLStatement createInsertQuery(Entity entity)
    throws SQLBuilderException
    {
        try {
            StringBuilder sqlStmt = new StringBuilder();

            // Construye la parte inicial del INSERT con el nombre de la tabla
            sqlStmt.append("INSERT INTO " + entity.getTableName() + " (");

            // StringBuilder para columnas y marcadores
            StringBuilder sqlColumns = new StringBuilder();
            StringBuilder sqlPlaceholders = new StringBuilder();
            List<Object> params = new ArrayList<>();

            // Recorre los campos de la entidad y arma las listas de columnas y valores
            for (Field field : EntityMapper.mapEntityAttributes(entity.getClass())) {

                // Si la entidad tiene autoincrementable y el campo es la PK, se omite
                if (entity.getAutoIncrement() && field.getName().equals(entity.getPrimaryKey())) {
                    continue;
                }

                Object value = readField(entity, field);
                if (value == null) {
                    continue;
                }

                sqlColumns.append(field.getName() + ", ");
                sqlPlaceholders.append("?, ");
                params.add(value);
            }

            if (params.isEmpty()) {
                throw new SQLBuilderException("Entity has no values to insert");
            }

            // Completa la sentencia INSERT eliminando la última coma y espacio
            sqlStmt.append(
                    sqlColumns.substring(0, sqlColumns.length() - 2)
                            + ") VALUES ("
                            + sqlPlaceholders.substring(0, sqlPlaceholders.length() - 2) + ");");

            return new SQLStatement(sqlStmt.toString(), params);
        }catch (SQLBuilderException error){
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }
    /**
     * Construye una sentencia UPDATE para la entidad especificada y el id dado.
     * Solo se actualizan los campos cuyo valor no es nulo.
     *
     * @param entity Entidad con los nuevos valores.
     * @param id     Identificador (valor) de la fila a actualizar.
     * @return Sentencia UPDATE parametrizada.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo o ocurre un error.
     */
    @Override
    public SQLStatement createUpdateQuery(Entity entity, String id)
    throws SQLBuilderException
    {
        try {

            if(entity == null || id == null){
                throw new SQLBuilderException("Invalid Values Provided");
            }

            StringBuilder sqlStmt = new StringBuilder();

                // Inicia la sentencia UPDATE con el nombre de la tabla y la cláusula SET
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                Field[] entityAttributes = EntityMapper.mapEntityAttributes(entity.getClass());

                // StringBuilder para los pares columna = ?
                StringBuilder sqlColumnsAndValues = new StringBuilder();
                List<Object> params = new ArrayList<>();

                // Recorre los campos y añade solo los que tengan valor
                for (Field field : entityAttributes) {
                    Object value = readField(entity, field);
                    if (value != null) {
                        sqlColumnsAndValues.append(field.getName() + " = ?, ");
                        params.add(value);
                    }
                }

                if (params.isEmpty()) {
                    throw new SQLBuilderException("Entity has no values to update");
                }

                // Completa la sentencia eliminando la última coma y espacio, y añade la cláusula WHERE
                Field primaryKey = findColumn(entityAttributes, entity.getPrimaryKey());
                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + primaryKey.getName() + " = ?;");
                params.add(toColumnType(id, primaryKey));

            return new SQLStatement(sqlStmt.toString(), params);
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
     * @return Sentencia DELETE parametrizada.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo.
     */
    @Override
    public SQLStatement createDeleteQuery(Entity entity, String id) throws SQLBuilderException
    {
        try {
            if (entity == null || id == null){
                throw new SQLBuilderException("Invalid values provided");
            }
            Field primaryKey = findColumn(EntityMapper.mapEntityAttributes(entity.getClass()), entity.getPrimaryKey());

            return new SQLStatement(
                    "DELETE FROM " + entity.getTableName() + " WHERE " + primaryKey.getName() + " = ?;",
                    List.of(toColumnType(id, primaryKey)));
        }catch (SQLBuilderException error){
            Logger.errorLog("SQLBuilder Exception", error.getMessage());
            throw new SQLBuilderException("SQLBuilder Exception: " + error.getMessage());
        }
    }

    /**
     * Busca la columna por nombre entre los campos de la entidad.
     * Es la lista blanca que impide inyectar SQL a través de nombres de columna.
     */
    private static Field findColumn(Field[] entityAttributes, String columnName) {
        for (Field field : entityAttributes) {
            if (field.getName().equals(columnName)) {
                return field;
            }
        }
        throw new SQLBuilderException("Unknown column: " + columnName);
    }

    /**
     * Lee el valor de un campo público de la entidad.
     */
    private static Object readField(Entity entity, Field field) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException error) {
            throw new SQLBuilderException("Cannot read field " + field.getName() + ": " + error.getMessage());
        }
    }

    /**
     * Convierte un valor recibido como texto (ids, filtros) al tipo Java del campo,
     * para que el driver lo envíe con el tipo correcto (por ejemplo INTEGER y no VARCHAR).
     */
    private static Object toColumnType(String value, Field field) {
        Class<?> type = field.getType();
        try {
            if (type == Integer.class || type == int.class) return Integer.valueOf(value);
            if (type == Long.class || type == long.class) return Long.valueOf(value);
            if (type == Short.class || type == short.class) return Short.valueOf(value);
            if (type == Double.class || type == double.class) return Double.valueOf(value);
            if (type == Float.class || type == float.class) return Float.valueOf(value);
            if (type == Boolean.class || type == boolean.class) return Boolean.valueOf(value);
            if (type == BigDecimal.class) return new BigDecimal(value);
            return value;
        } catch (NumberFormatException error) {
            throw new SQLBuilderException("Invalid value for column " + field.getName() + ": " + value);
        }
    }
}
