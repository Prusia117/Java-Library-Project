package com.library.kernel.kernelSql.orm.core;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.library.kernel.classes.Colors;
import com.library.kernel.kernelSql.u;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.connection.DatabaseProperties;
import com.library.kernel.kernelSql.orm.core.sql.SQLBuilder;
import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.entity.EntityFactory;
import com.library.kernel.kernelSql.orm.entity.EntityMapper;
import com.library.kernel.kernelSql.orm.entity.EntityPopulator;
import com.library.kernel.kernelSql.orm.entity.utils.EntityFactoryException;
import com.library.kernel.kernelSql.orm.entity.utils.EntityMapperException;
import com.library.kernel.kernelSql.orm.entity.utils.EntityPopulatorException;

/*
 * Se declara un generic T que extiende de Entity es decir:
 * Es un tipo desconosido pero que tiene que se una implentación
 * o una herencia de Entity
 */
public final class ORM<T extends Entity> implements Colors {

    // Constante para definir si el ORM activa el modo DEBUG en el cual lanzara mensajes de información
    private static final boolean DEBUG_MODE = true;

    private static ConnectionProvider connectionProvider = null;

    // Método para setear los parametros de la base de datos
    public static void setDatabaseProperties(DatabaseProperties properties) {
        if (connectionProvider == null){
            connectionProvider = new ConnectionProvider(properties);
        }
    }

    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public ArrayList<T> selectQuery(T entity, String value, String field)
    throws ORMException
    {
        try {

            if(entity == null){
                throw new ORMException("Invalid Values Provided");
            }

            String sqlStmt = SQLBuilder.createSelectQuery(entity, value, field);

            /*
             * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
             * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
             * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
             * try de esta manera ejecuta el close al final por ti
             */
            try (
                    // Se obtiene una conexión
                    Connection connection = connectionProvider.getConnection();
                    Statement stmt = connection.createStatement();
                    // Se ejecuta el sql statment
                    ResultSet resultSet = stmt.executeQuery(sqlStmt);

            ) {

                if (DEBUG_MODE) {
                    Logger.infoLog("SQL Query", sqlStmt);
                }

                // Se crea un arraylist de tipo T consulta mas del tipo T al principio del archivo
                ArrayList<T> entityArray = new ArrayList<T>();

                Field[] entityFields = EntityMapper.mapEntityAttributes(entity.getClass());

                if (DEBUG_MODE) {
                    Logger.infoLog(GREEN + "Fields from Entity", String.valueOf(entityFields.length));
                }

                // Se crea un while que dice mientras existe un resultado en el resultSet continua
                while (resultSet.next()) {

                    // Se crea una nueva entity de tipo T consulta para mas información la clase EntityFactory
                    T newEntity = EntityFactory.createEntity(entity.getClass());

                    // Se obtienen las entities relacionadas con la entity actual
                    HashMap<Class<?>, String> relationshipsEntities = newEntity.getRelationships();

                    // Se crea un arraylist que almacenera las entities relacionadas con la entity actual
                    ArrayList<Entity> relationshipEntities = new ArrayList<Entity>();

                    /*
                     * Se crea un for el cual recorrera la cantidad de n veces el numero de atributos de la entity - 1
                     * ya que el campo entityRelationships no cuenta como columna de la entity el for recorrera la entity creada
                     * y la llenara con los datos obteneidos de la base de datos
                     */
                    for (int i = 0; i < entityFields.length - 1; i++) {
                        if (DEBUG_MODE) {
                            Logger.infoLog(
                                    YELLOW + "Entity Info",
                                    "Entity Field => " + entityFields[i].getName() + RESET + " | " + PURPLE
                                            + "Entity Value => " + resultSet.getObject(i + 1)
                            );
                        }
                        // Se setea los valores de las filas en el objeto
                        EntityPopulator.populateEntity(newEntity, entityFields[i], resultSet.getObject(i + 1));
                    }

                    // Se crea un auxiliar para el siguiente for
                    int auxiliar = entityFields.length;

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

                            for (int j = 0; j < relationshipEntityFields.length - 1; j++) {
                                if (DEBUG_MODE) {
                                    Logger.infoLog(
                                            YELLOW + "Entity Field",
                                            relationshipEntityFields[j].getName() + RESET
                                                    + " | "
                                                    + PURPLE
                                                    + "Entity Value => " + resultSet.getObject(j + 1)
                                    );
                                }
                                EntityPopulator.populateEntity(newRelationshipEntity, relationshipEntityFields[j], resultSet.getObject(auxiliar++));
                            }
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

            } catch (SQLException | EntityMapperException | EntityFactoryException | EntityPopulatorException error) {

                Logger.infoLog("SQL Query", sqlStmt);
                Logger.infoLog("SQL Error Query Exception", error.getMessage());
                throw new ORMException("SQL Error Query Exception: " + error.getMessage());
            }
        } catch (ORMException error) {
            Logger.infoLog("SQL Exception", error.getMessage());
            throw new ORMException("SQL Exception: " + error.getMessage());
        }
    }

    /*
     * Método updateQuery funciona para insertar o actualizar regsitro
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public int updateQuery(T entity, String id)
    throws ORMException
    {
        try {
            // Se crea el string que almacena el sql statment de la cosnulta
            String sqlStmt = null;

            // Si la id es null indica que es una insert de un nuevo registro
            if (id == null && entity != null) {
                sqlStmt = SQLBuilder.createUpdateQuery(entity, null);
            } else if (id != null && entity != null) {
                sqlStmt = SQLBuilder.createUpdateQuery(entity, id);
            } else {
                // Si no es un caso de insert o update se lanza una excepción
                throw new ORMException("Invalid Values Provided");
            }

            if (DEBUG_MODE) {
                Logger.infoLog("SQL Query", sqlStmt);
            }

            /*
             * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
             * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
             * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
             * try de esta manera ejecuta el close al final por ti
             */
            try (
                    Connection connection = connectionProvider.getConnection();
                    Statement stmt = connection.createStatement();) {
                // Se ejecuta el sqlstatment
                int result = stmt.executeUpdate(sqlStmt);

                return result;

            } catch (SQLException error) {

                Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
                throw new ORMException("ORM SQL Error Query Exception: " + error.getMessage());
            }
        } catch (ORMException | EntityMapperException error) {
            Logger.errorLog("ORM Exception", error.getMessage());
            throw new ORMException("ORM Exception: " + error.getMessage());
        }
    }
    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public int deleteQuery(T entity, String id)
    throws ORMException
    {
        try {
            // Si no se proporciono ningun parametro se lanza una excepción
            if (entity == null || id == null){
                throw new ORMException("Invalid Values Provided");
            }

            // Se crea el sql statment de un delete
            String sqlStmt = SQLBuilder.createDeleteQuery(entity, id);

            /*
            * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
            * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
            * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
            * try de esta manera ejecuta el close al final por ti
            */
            try (
                    Connection connection = connectionProvider.getConnection();
                    Statement stmt = connection.createStatement();) {
                
                // Se ejecuta el sql statment
                int result = stmt.executeUpdate(sqlStmt);

                if (DEBUG_MODE) {
                    Logger.infoLog("SQL Query", sqlStmt);
                }
                return result;

            } catch (SQLException error) {

                Logger.errorLog("ORM SQL Error Query Exception", error.getMessage());
                throw new ORMException("ORM SQL Error Query Exception: " + error.getMessage());
            }
        } catch (ORMException error) {
            Logger.errorLog("ORM Exception", error.getMessage());
            throw new ORMException("ORM Exception: " + error.getMessage());
        }
    }

}