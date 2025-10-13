package com.library.kernel.models;

import java.lang.reflect.Field;

import com.library.kernel.orm.ORM;


import java.sql.*;

public class Model extends ORM{

    protected String tableName;

    protected String primaryKey;

    protected String[] excluedFields;

    protected Model(String name){
        super();
    }

    protected void findById(int id) throws SQLException
    {
        String sql = "SELECT * FROM " + tableName + " WHERE " + primaryKey + " = " + id;
        this.query(tableName);
    }

    // TODO: Agregar funcionamineto para buscar por cierta columna y valor
    protected void findByField() throws SQLException
    {
        
    }

    protected void save()
    {

        Class<?> clss = this.getClass();

        Field[] publicFields = clss.getFields();

        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");

        for (Field field : publicFields) sql.append(field + ", ");

        sql.append(") VALUES(");


        for (Field field : publicFields) {
            Class<?> propertyField = field.get(clss);
            sql.append("");
        }

        
    }


}
