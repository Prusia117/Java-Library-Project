package com.library.kernel.kernelSql.orm.entity;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Interfaz Entity: Una Entity es la representación de una tabla de una base de datos a POO
 */
public abstract interface Entity {
    
    // Método para obtener la primary key de la entity
    public abstract String getPrimaryKey();

    // Método para obtener el nombre de la tabla de la entity
    public abstract String getTableName();

    // Método para obtener si la entity tiene un id de tipo autoincrementable
    public abstract boolean getAutoIncrement();

    // Método para obtener las relaciones de la entity
    public abstract HashMap<Class<?>, String> getRelationships();

    // Método para obtener anadir las entities relacionadas a la principal
    public abstract  void setEntityRelationships(ArrayList<Entity> entities);

}
