package com.library.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.library.kernel.orm.Entity;

public class BookEntity implements Entity
{
    private String tableName = "books";

    private String primaryKey = "book_id";

    private boolean hasAutoIncrement = true;

    private HashMap<Class<?>, String> relationships = new HashMap<Class<?>, String>() {{
        put(AutorEntity.class, "bo_autor_id");
    }};

    public Integer book_id;

    public String tittle;

    public String img;

    public Integer bo_autor_id;

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