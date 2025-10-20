package com.library.kernel.orm;

import java.util.ArrayList;
import java.util.HashMap;

public abstract interface Entity {
    
    public abstract String getPrimaryKey();

    public abstract String getTableName();

    public abstract boolean getAutoIncrement();

    public abstract  HashMap<Class<?>, String> getRelationships();

    public abstract  void setEntityRelationships(ArrayList<Entity> entities);

}
