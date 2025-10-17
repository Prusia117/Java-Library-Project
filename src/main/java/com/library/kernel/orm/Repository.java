package com.library.kernel.orm;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Repository<T extends Entity> 
{

    private ORM orm;

    private Class<T> type;

    Repository(Class<T> type){
        orm = new ORM();

        this.type = type;
    }
    
    public List<T> getAll()
    {
        try {
            ArrayList<T> rs = orm.selectQuery(EntityFactory.createEntity(type),null);
            
            return rs;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
/*
    public abstract T getById();

    public abstract T getByName();

    public abstract boolean save(T object);

    public abstract boolean update(T object, int id);

    public abstract boolean delete(int id);*/
}
