package com.library.kernel.orm;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Repository<T extends Entity> 
{

    private ORM orm;

    Repository(){
        orm = new ORM();
    }
    
    public List<T> getAll() throws SQLException
    {
        try {
            
            ResultSet rs = orm.selectQuery(null, null);
            
            ArrayList<Entity> entityArray = new ArrayList<Entity>();

            while(rs.next()){
                
                Entity newEntity = new Entity();
            
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
    }
/*
    public abstract T getById();

    public abstract T getByName();

    public abstract boolean save(T object);

    public abstract boolean update(T object, int id);

    public abstract boolean delete(int id);*/
}
