package com.library.kernel.models;

public class Model {

    protected String tableName;


    public Model(String name){
        super();
        
    }


    public String query(String sqlStatment ){
    
        return sqlStatment; 
    }


    public String select(String sqlStatment ){

        if(sqlStatment == null) return this.query("SELECT * FROM" + this.tableName);
    
        return this.query(sqlStatment);
    }

    public String insert(String sqlStatment ){

        if(sqlStatment == null) return "Error";
    
        return "INSERT * FROM" + this.tableName + "WHERE " + sqlStatment; 
    }

    public String update(String filters ){

        if(filters == null) return "SELECT * FROM" + this.tableName;
    
        return "SELECT * FROM" + this.tableName + "WHERE " + filters; 
    }

    public String where(String filters ){

        if(filters == null) return "SELECT * FROM" + this.tableName;
    
        return "SELECT * FROM" + this.tableName + "WHERE " + filters; 
    }

    
}
