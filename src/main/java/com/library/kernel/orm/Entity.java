package com.library.kernel.orm;

public abstract interface Entity {
    
    public abstract String getPrimaryKey();

    public abstract String getTableName();
}
