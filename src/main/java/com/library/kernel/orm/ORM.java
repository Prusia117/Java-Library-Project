package com.library.kernel.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ORM<T extends Entity> {

    private static SQLDriver sqlDriver = SQLDriver.GENERIC;

    private static DatabaseProperties databaseProperties = null;

    private Connection getConnection() throws SQLException {
        try {

            String stringConnection = "jdbc:" + sqlDriver.getDriver() + "://";
            Connection connection = DriverManager.getConnection(stringConnection, databaseProperties.getDataBaseUser(),
                    databaseProperties.getDataBasePassword());

            return connection;

        } catch (SQLException error) {

            System.out.println("SQL Error Connection:" + error);
            throw error;
        }
    }

    public ArrayList<T> selectQuery(T entity, String id)
            throws SQLException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException {

        StringBuilder sqlStmt = new StringBuilder("SELECT * FROM " + entity.getTableName());

        if (id != null)
            sqlStmt.append(" WHERE " + entity.getPrimaryKey() + " = " + id);

        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(sqlStmt.toString());

        ) {

            ArrayList<T> entityArray = new ArrayList<T>();

            Class<?> entityClass = entity.getClass();

            Field[] entityFields = entityClass.getFields();

            while (resultSet.next()) {

                T newEntity = EntityFactory.createEntity(entity.getClass());
                for (int i = 0; i < entityFields.length; i++) {
                    entityFields[i].set(newEntity, resultSet.getObject(i));
                }

                entityArray.add(newEntity);

            }

            return entityArray;

        } catch (SQLException error) {

            System.out.println("SQL Error Query Execution:" + error);
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
                    sqlColumns.append(entry.getKey() + ", ");
                    sqlValues.append(entry.getValue() + ", ");
                }

                sqlStmt.append(
                        sqlColumns.substring(0, sqlColumns.length() - 2)
                                + ") VALUES ("
                                + sqlValues.substring(0, sqlValues.length() - 2));
            } else if (id != null && entity != null) {
                sqlStmt.append("UPDATE " + entity.getTableName() + " SET ");

                HashMap<String, String> map = EntityFactory.entityMapper(entity);

                StringBuilder sqlColumnsAndValues = new StringBuilder();
                for (Map.Entry<String, String> entry : map.entrySet()) {

                    if (entry.getValue() != null) {
                        sqlColumnsAndValues.append(entry.getKey() + " = " + entry.getValue() + ", ");
                    }
                }

                sqlStmt.append(
                        sqlColumnsAndValues.substring(0, sqlColumnsAndValues.length() - 2)
                                + " WHERE " + entity.getPrimaryKey() + " = " + id);

            } else {
                throw new SQLException("Invalid Query Statment");
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException error) {
            System.out.println("SQL Error Query Execution:" + error);
            return 0;
        }

        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();) {
            int result = stmt.executeUpdate(sqlStmt.toString());
            return result;

        } catch (SQLException error) {

            System.out.println("SQL Error Query Execution:" + error);
            return 0;
        }
    }

    public int deleteQuery(T entity, String id) throws SQLException {

        StringBuilder sqlStmt = new StringBuilder(
                "DELETE FROM " + entity.getTableName() + " WHERE " + entity.getPrimaryKey() + " = " + id);

        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();) {
            int result = stmt.executeUpdate(sqlStmt.toString());
            return result;

        } catch (SQLException error) {

            System.out.println("SQL Error Query Execution:" + error);
            return 0;
        }
    }

    public static void setDatabaseProperties(DatabaseProperties properties) {

        if (databaseProperties == null)
            databaseProperties = properties;

    }

}