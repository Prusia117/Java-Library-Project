package com.library.kernel.daos;

class Dao{

    protected String tableName;

    public String select(String filters ){

        if(filters == null) return "SELECT * FROM" + this.tableName;
    
        return "SELECT * FROM" + this.tableName + "WHERE " + filters; 
    }

    public String insert(String filters ){

        if(filters == null) return "SELECT * FROM" + this.tableName;
    
        return "SELECT * FROM" + this.tableName + "WHERE " + filters; 
    }

    public String update(String filters ){

        if(filters == null) return "SELECT * FROM" + this.tableName;
    
        return "SELECT * FROM" + this.tableName + "WHERE " + filters; 
    }

}