package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.library.kernel.classes.Colors;

public final class ORM<T extends Entity> implements Colors {

    private static DatabaseProperties databaseProperties = null;

    private static final boolean DEBUG_MODE = true;

    private Connection getConnection() throws SQLException {
        try {

            String stringConnection = "jdbc:" + databaseProperties.getSQLDriver() 
                    + "://"+ databaseProperties.getHost() + ":" + databaseProperties.getPort() + "/" + databaseProperties.getDataBaseName();
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(), databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {

            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ArrayList<T> selectQuery(T entity, String id)
            throws SQLException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException {

        HashMap<String, String> map = EntityFactory.entityMapper(entity);

        StringBuilder sqlColumns = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() != "entityRelationships") {
                sqlColumns.append(entry.getKey() + ", ");
            }
        }

        StringBuilder joins = new StringBuilder();

        StringBuilder joinsSqlColumns = new StringBuilder();

        if (entity.getRelationships() != null) {
            HashMap<Class<?>, String> relationships = entity.getRelationships();

            for (Map.Entry<Class<?>, String> relationship : relationships.entrySet()) {
                Entity newRelationalEntity = EntityFactory.createEntity(relationship.getKey());
                joins.append(
                        " INNER JOIN " + newRelationalEntity.getTableName() + " ON "
                                + newRelationalEntity.getPrimaryKey() + " = " + relationship.getValue());

                HashMap<String, String> mapjoinsSqlColumns = EntityFactory.entityMapper(newRelationalEntity);

                for (Map.Entry<String, String> entry : mapjoinsSqlColumns.entrySet()) {
                    if (entry.getKey() != "entityRelationships") {
                        joinsSqlColumns.append(entry.getKey() + ", ");
                    }
                }
            }

        }
        StringBuilder sqlStmt = new StringBuilder();
        if (entity.getRelationships() != null) {
            sqlStmt.append(
                    "SELECT " + sqlColumns + joinsSqlColumns.substring(0, joinsSqlColumns.length() - 2) + " FROM "
                            + entity.getTableName());
        } else {
            sqlStmt.append(
                    "SELECT " + sqlColumns.substring(0, sqlColumns.length() - 2) + " FROM " + entity.getTableName());
        }
        if (id != null)
            sqlStmt.append(" WHERE " + entity.getPrimaryKey() + " = " + id);

        if (entity.getRelationships() != null)
            sqlStmt.append(joins);

        sqlStmt.append(" ORDER BY " + entity.getPrimaryKey() + " ASC;");

        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlStmt.toString());

        ) {

            if (DEBUG_MODE) {
                System.out.println(CIAN + "SQL Query:" + RESET);
                System.out.println(BLUE + sqlStmt + RESET);
            }

            ArrayList<T> entityArray = new ArrayList<T>();

            Class<?> entityClass = entity.getClass();

            Field[] entityFields = entityClass.getFields();

            if (DEBUG_MODE)
                System.out.println(GREEN + "Fields from Entity => " + entityFields.length + RESET);

            while (resultSet.next()) {

                T newEntity = EntityFactory.createEntity(entity.getClass());

                HashMap<Class<?>, String> relationshipsEntities = newEntity.getRelationships();

                ArrayList<Entity> relationshipEntities = new ArrayList<Entity>();

                for (int i = 0; i < entityFields.length - 1; i++) {
                    if (!DEBUG_MODE) {
                        System.out.println(
                                YELLOW + "Entity Field => " + entityFields[i].getName() + RESET + " | " + PURPLE
                                        + "Entity Value => " + resultSet.getObject(i + 1) + RESET);
                    }
                    entityFields[i].set(newEntity, resultSet.getObject(i + 1));
                }

                int auxiliar = entityFields.length;

                for (Map.Entry<Class<?>, String> relationship : relationshipsEntities.entrySet()) {

                    Class<?> newClassEntity = relationship.getKey();

                    Field[] relationshipEntityFields = newClassEntity.getFields();

                    T newRelationshipEntity = EntityFactory.createEntity(newClassEntity);

                    for (int j = 0; j < relationshipEntityFields.length - 1; j++) {
                        if (!DEBUG_MODE) {
                            System.out.println(
                                    YELLOW + "Entity Field => " + relationshipEntityFields[j].getName() + RESET + " | "
                                            + PURPLE
                                            + "Entity Value => " + resultSet.getObject(j + 1) + RESET);
                        }
                        relationshipEntityFields[j].set(newRelationshipEntity, resultSet.getObject(auxiliar++));
                    }
                    relationshipEntities.add(newRelationshipEntity);
                }

                newEntity.setEntityRelationships(relationshipEntities);

                entityArray.add(newEntity);

            }

            return entityArray;

        } catch (SQLException error) {

            System.out.println(RED + "SQL Error Query Exeption: " + error + RESET);
            return null;
        }
    }

    public int updateQuery(T entity, String id) throws SQLException {

        StringBuilder sqlStmt = new StringBuilder();

        try {
            if (id == null) {
                sqlStmt.append("INSERT INTO " + entity.getTableName() + " (");

                HashMap<String, String> map = EntityFactory.entityMapper(entity);

                StringBuilder sqlColumns = new StringBuilder();
                StringBuilder sqlValues = new StringBuilder();

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entity.getAutoIncrement() && entry.getKey() == entity.getPrimaryKey()
                            && entry.getKey() != "entityRelationships") {
                        continue;
                    }
                    sqlColumns.append(entry.getKey() + ", ");
                    sqlValues.append("'" + entry.getValue() + "'" + ", ");
                }

                sqlStmt.append(
                        sqlColumns.substring(0, sqlColumns.length() - 2)
                                + ") VALUES ("
                                + sqlValues.substring(0, sqlValues.length() - 2) + ");");
            } else if (id != null && entity != null) {
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                HashMap<String, String> map = EntityFactory.entityMapper(entity);

                StringBuilder sqlColumnsAndValues = new StringBuilder();
                for (Map.Entry<String, String> entry : map.entrySet()) {

                    if (entry.getValue() != null) {
                        sqlColumnsAndValues.append(entry.getKey() + " = '" + entry.getValue() + "', ");
                    }
                }

                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            } else {
                throw new SQLException("Invalid Query Statment");
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException error) {
            System.out.println(RED + "SQL Error Query Exeption:" + error + RESET);
            return 0;
        }

        if (DEBUG_MODE) {
            System.out.println(CIAN + "SQL Query:" + RESET);
            System.out.println(BLUE + sqlStmt + RESET);
        }
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();) {
            int result = stmt.executeUpdate(sqlStmt.toString());

            return result;

        } catch (SQLException error) {

            System.out.println(RED + "SQL Error Query Execution:" + error + RESET);
            return 0;
        }
    }

    public int deleteQuery(T entity, String id) throws SQLException {

        try {

            if (entity == null || id == null)
                throw new SQLException("Invalid values provided");

            StringBuilder sqlStmt = new StringBuilder(
                    "DELETE FROM " + entity.getTableName() + " WHERE " + entity.getPrimaryKey() + " = " + id + ";");

            try (
                    Connection connection = getConnection();
                    Statement stmt = connection.createStatement();) {
                int result = stmt.executeUpdate(sqlStmt.toString());

                if (DEBUG_MODE) {
                    System.out.println(CIAN + "SQL Query:" + RESET);
                    System.out.println(BLUE + sqlStmt + RESET);
                }
                return result;

            } catch (SQLException error) {

                System.out.println(RED + "SQL Error Query Execution:" + error + RESET);
                return 0;
            }
        } catch (SQLException e) {
            // TODO: Logger implementation
            System.out.println(RED + "SQL Error Query Execution:" + e + RESET);
            return 0;
        }
    }

    public static void setDatabaseProperties(DatabaseProperties properties) {

        if (databaseProperties == null)
            databaseProperties = properties;

    }

}