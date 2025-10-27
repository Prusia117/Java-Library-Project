package com.library.examples;

import java.util.ArrayList;
import java.util.HashMap;

import com.library.kernel.kernelSql.orm.entity.Entity;

public class AutorEntity implements Entity
{
    private String tableName = "autores";

    private String primaryKey = "autor_id";

    private HashMap<Class<?>, String> relationships = new HashMap<Class<?>, String>() {{
        put(BookEntity.class, "book_id");
    }};


    private boolean hasAutoIncrement = true;

    public Integer autor_id;

    public String nombre;

    public ArrayList<Entity> entityRelationships;

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean getAutoIncrement() {
        return hasAutoIncrement;
    }

    public HashMap<Class<?>, String> getRelationships(){
        return relationships;
    }

    public void setEntityRelationships(ArrayList<Entity> entities){
        this.entityRelationships = entities;
    }

}
