package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.library.kernel.classes.Colors;

/*
 * Se declara un generic T que extiende de Entity es decir:
 * Es un tipo desconosido pero que tiene que se una implentación
 * o una herencia de Entity
 */
public final class ORM<T extends Entity> implements Colors {

    // Object tipo DatabaseProperties consulta su interfaz par mas información
    private static DatabaseProperties databaseProperties = null;

    // Constante para definir si el ORM activa el modo DEBUG en el cual lanzara mensajes de información
    private static final boolean DEBUG_MODE = true;

    // Método para setear los parametros de la base de datos
    public static void setDatabaseProperties(DatabaseProperties properties) {

        if (databaseProperties == null){
            databaseProperties = properties;
        }
    }

    // Método para obtener una conexión utilizando el driver de postgres JDBC https://jdbc.postgresql.org/
    private Connection getConnection() throws SQLException {
        try {
            /*
             * Se crea el "string" de conexión con estructura:
             * jdbc:'driverName'://'host':'port'/'database'
             */
            String stringConnection = "jdbc:" + databaseProperties.getSQLDriver()
                    + "://" + databaseProperties.getHost() + ":" + databaseProperties.getPort() + "/"
                    + databaseProperties.getDataBaseName();

            // Se crea una conexión y se le pasa al método getConnection el string conexión como otros parametros como el usario y contraseña
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(),
                    databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {

            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error:" + error + RESET);
            throw error;
        }
    }

    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public ArrayList<T> selectQuery(T entity, String id)
            throws SQLException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException {

        // Se mapea la entidad consulta la clase par mas información
        HashMap<String, String> map = EntityFactory.entityMapper(entity);

        // Se crea el string que almacena las columnas de la tabla es StringBuilder para preservar la dirección de memoria
        StringBuilder sqlColumns = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            // Se omite el campo entityRelationships ya que contiene las relaciones de las tablas
            if (entry.getKey() != "entityRelationships") {
                sqlColumns.append(entry.getKey() + ", ");
            }
        }
        // Se crea el string que almacena los joins de la tabla es StringBuilder para preservar la dirección de memoria
        StringBuilder joins = new StringBuilder();

        // Se crea el string que almacena las columnas de las tablas externas es StringBuilder para preservar la dirección de memoria
        StringBuilder joinsSqlColumns = new StringBuilder();

        // Se comprueba si la tabla tiene relaciones
        if (entity.getRelationships() != null) {

            /*
             * Se obtiene un hashmap con las relaciones de la tabla:
             * Class<?> representa a la entity externa
             * String representa el campo por el cual la tabla dependiente se relaciona
             * con la tabla independiente
             */
            HashMap<Class<?>, String> relationships = entity.getRelationships();

            // Se recorre el hashmap con un foreach
            for (Map.Entry<Class<?>, String> relationship : relationships.entrySet()) {

                // Se crea una nueva entity de tipo Class<?> consulta la clase par mas información
                Entity newRelationalEntity = EntityFactory.createEntity(relationship.getKey());

                // Se agrega los joins de la tabla
                joins.append(
                        " INNER JOIN " + newRelationalEntity.getTableName() + " ON "
                                + newRelationalEntity.getPrimaryKey() + " = " + relationship.getValue());

                // Se mapea la nueva entity consulta la clase par mas información
                HashMap<String, String> mapjoinsSqlColumns = EntityFactory.entityMapper(newRelationalEntity);

                // Se recorre el hashmap con un foreach
                for (Map.Entry<String, String> entry : mapjoinsSqlColumns.entrySet()) {
                    // Se omite el campo entityRelationships ya que contiene las relaciones de las tablas
                    if (entry.getKey() != "entityRelationships") {
                        joinsSqlColumns.append(entry.getKey() + ", ");
                    }
                }
            }

        }

        // Se crea el string que almacena el sql statment de la cosnulta es
        // StringBuilder para preservar la dirección de memoria
        StringBuilder sqlStmt = new StringBuilder();

        // Se comprueba si la tabla tiene relaciones
        if (entity.getRelationships() != null) {
            // agrega los joins correspondientes de la tabla
            sqlStmt.append(
                    "SELECT " + sqlColumns + joinsSqlColumns.substring(0, joinsSqlColumns.length() - 2) + " FROM "
                            + entity.getTableName());
        } else {
            // agrega un select con solo las columnas de la tabla dependiente
            sqlStmt.append(
                    "SELECT " + sqlColumns.substring(0, sqlColumns.length() - 2) + " FROM " + entity.getTableName());
        }

        // Si es un select por id se agrega la sentencia where
        if (id != null) {
            sqlStmt.append(" WHERE " + entity.getPrimaryKey() + " = " + id);
        }

        // Si tiene relaciones se agregan al sql statment de la cosnulta
        if (entity.getRelationships() != null) {
            sqlStmt.append(joins);
        }

        // Si agrega un order by por el id de tipo ASC
        sqlStmt.append(" ORDER BY " + entity.getPrimaryKey() + " ASC;");

        /*
         * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
         * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
         * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
         * try de esta manera ejecuta el close al final por ti
         */
        try (
                // Se obtiene una conexión
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                // Se ejecuta el sql statment
                ResultSet resultSet = stmt.executeQuery(sqlStmt.toString());

        ) {

            if (DEBUG_MODE) {
                System.out.println("[" + BLUE + "INFO" + RESET + "]: " + CIAN + "SQL Query:" + BLUE + sqlStmt + RESET);
            }

            // Se crea un arraylist de tipo T consulta mas del tipo T al principio del archivo
            ArrayList<T> entityArray = new ArrayList<T>();

            // Se obtiene la clase de la entity de la que le pasamos
            Class<?> entityClass = entity.getClass();

            // Obtenemos los atributos de la clase consulta para mas información el archivo en examples/GetFieldNames
            Field[] entityFields = entityClass.getFields();

            if (DEBUG_MODE) {
                System.out.println("[" + BLUE + "INFO" + RESET + "]: " + GREEN + "Fields from Entity => " + entityFields.length + RESET);
            }

            // Se crea un while que dice mientras existe un resultado en el resultSet continua
            while (resultSet.next()) {

                // Se crea una nueva entity de tipo T consulta para mas información la clase EntityFactory
                T newEntity = EntityFactory.createEntity(entity.getClass());

                // Se obtienen las entity's relacionadas con la entity actual
                HashMap<Class<?>, String> relationshipsEntities = newEntity.getRelationships();

                // Se crea un arraylist que almacenera las entity's relacionadas con la entity actual
                ArrayList<Entity> relationshipEntities = new ArrayList<Entity>();

                /*
                 * Se crea un for el cual recorrera la cantidad de n veces el numero de atributos de la entity - 1
                 * ya que el campo entityRelationships no cuenta como columna de la entity el for recorrera la entity creada
                 * y la llenara con los datos obteneidos de la base de datos
                 */
                for (int i = 0; i < entityFields.length - 1; i++) {
                    if (DEBUG_MODE) {
                        System.out.println(
                                "[" + BLUE + "INFO" + RESET + "]: " +
                                YELLOW + "Entity Field => " + entityFields[i].getName() + RESET + " | " + PURPLE
                                        + "Entity Value => " + resultSet.getObject(i + 1) + RESET);
                    }
                    // Se setea los valores de las filas en el objeto
                    entityFields[i].set(newEntity, resultSet.getObject(i + 1));
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

                        Field[] relationshipEntityFields = newClassEntity.getFields();

                        T newRelationshipEntity = EntityFactory.createEntity(newClassEntity);

                        for (int j = 0; j < relationshipEntityFields.length - 1; j++) {
                            if (!DEBUG_MODE) {
                                System.out.println(
                                        "[" + BLUE + "INFO" + RESET + "]: " +
                                        YELLOW + "Entity Field => " + relationshipEntityFields[j].getName() + RESET
                                                + " | "
                                                + PURPLE
                                                + "Entity Value => " + resultSet.getObject(j + 1) + RESET);
                            }
                            relationshipEntityFields[j].set(newRelationshipEntity, resultSet.getObject(auxiliar++));
                        }
                        // Se gaurda la entity relacionada con la nueva entity actual en el array anteriormente creado
                        relationshipEntities.add(newRelationshipEntity);
                    }
                    // Se agregan las entity's relacionadas con la nueva entity 
                    newEntity.setEntityRelationships(relationshipEntities);
                }
                // Se agrega la nueva entity al array de nuevas entity's
                entityArray.add(newEntity);

            }

            return entityArray;

        } catch (SQLException error) {

            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error Query Exeption: " + error + RESET);
            return null;
        }
    }

    /*
     * Método updateQuery funciona para insertar o actualizar regsitro
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public int updateQuery(T entity, String id) throws SQLException {

        // Se crea el string que almacena el sql statment de la cosnulta es
        // StringBuilder para preservar la dirección de memoria
        StringBuilder sqlStmt = new StringBuilder();

        try {
            // Si la id es null indica que es una insert de un nuevo registro
            if (id == null && entity != null) {
                // Se crea la base del sql Statmnet con la palabra insert y el nombre de la tabla
                sqlStmt.append("INSERT INTO " + entity.getTableName() + " (");
                
                //Se mapea la entity consulta la clase EntityFactory para mayor información
                HashMap<String, String> map = EntityFactory.entityMapper(entity);

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
            // Si ambas referencias no son null se trata de actualizar un regsitro
            } else if (id != null && entity != null) {
                
                // Se crea la base del sql Statmnet con la palabra update, el nombre de la tabla y la palabra set
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                //Se mapea la entity consulta la clase EntityFactory para mayor información
                HashMap<String, String> map = EntityFactory.entityMapper(entity);

                // Se crean el string que almacenara los atributos y los valores de la entity
                StringBuilder sqlColumnsAndValues = new StringBuilder();

                // Se recorre el map de la entity para asignarle los valores a sqlColumnsAndValues
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    // Si el valor es null significa que no es campo a actualizar de la entity
                    if (entry.getValue() != null) {
                        sqlColumnsAndValues.append(entry.getKey() + " = '" + entry.getValue() + "', ");
                    }
                }
                // Se completa el sql statment
                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            } else {                
                // Si no es un caso de insert o update se lanza una excepción
                throw new SQLException("Invalid Query statment");
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException error) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error Query Exeption:" + error + RESET);
            return 0;
        }

        if (DEBUG_MODE) {
            System.out.println("[" + BLUE + "INFO" + RESET + "]: " + CIAN + "SQL Query:" + BLUE + sqlStmt + RESET);
        }

        /*
         * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
         * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
         * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
         * try de esta manera ejecuta el close al final por ti
         */
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();) {
            // Se ejecuta el sqlstatment
            int result = stmt.executeUpdate(sqlStmt.toString());

            return result;

        } catch (SQLException error) {

            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error Query Execution:" + error + RESET);
            return 0;
        }
    }
    /*
     * Método selectQuery funciona para manejar selects de toda la data o solo de un registro con un id
     * Se le pasa una entity de tipo T y opcional un id de tipo String
     */
    public int deleteQuery(T entity, String id) throws SQLException {

        try {
            // Si no se proporciono ningun parametro se lanza una excepción
            if (entity == null || id == null){
                throw new SQLException("Invalid values provided");
            }

            // Se crea el sql statment de un delete
            StringBuilder sqlStmt = new StringBuilder(
                    "DELETE FROM " + entity.getTableName() + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            /*
            * Se crea un denominado 'try with resources' se denomina de esta manera ya que cierra por ti
            * los diferentes recursos ejemplo una conexión ya que no utilizamos un pool de estas
            * se necesita ejecutar al final de la transacción sql 'connection.close();' pero con utilizar el
            * try de esta manera ejecuta el close al final por ti
            */
            try (
                    Connection connection = getConnection();
                    Statement stmt = connection.createStatement();) {
                
                // Se ejecuta el sql statment
                int result = stmt.executeUpdate(sqlStmt.toString());

                if (DEBUG_MODE) {
                    System.out.println("[" + BLUE + "INFO" + RESET + "]: " + CIAN + "SQL Query:" + BLUE + sqlStmt + RESET);
                }
                return result;

            } catch (SQLException error) {

                System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error Query Execution:" + error + RESET);
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "SQL Error Query Execution:" + e + RESET);
            return 0;
        }
    }

}