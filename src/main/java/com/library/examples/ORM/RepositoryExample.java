package com.library.examples.ORM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.kernel.orm.Entity;
import com.library.kernel.orm.Repository;

public final class RepositoryExample extends Repository
{
    @Override
    public List<EntityExample> getAll() throws SQLException
    {

        EntityExample ee = new EntityExample();

        String table = ee.getTableName();


        try(
                ResultSet responce = getQuery("SELECT * FROM " + table);
        ){
        
        }
        catch(SQLException  q){
        
        }

        List<EntityExample> en =  new ArrayList<>();

        return en;
    }
}


final class EntityExample implements Entity
{

    private String primariKey;

    public String getPrimaryKey(){
        return primariKey;
    }

    private String tableName;

    public String getTableName(){
        return tableName;
    }

}