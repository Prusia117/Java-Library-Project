package com.library.kernel.orm;

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
    
    public List<T> getById(String id)
    {
        try {
            ArrayList<T> rs = orm.selectQuery(EntityFactory.createEntity(type),id);
            
            return rs;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    
    public boolean save(T entity)
    {
        try {
            int rs = orm.updateQuery(entity, null);
            
            return rs == 1 ? true : false;

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
    
    public boolean update(T entity, String id)
    {
        try {
            int rs = orm.updateQuery(entity, id);
            
            return rs == 1 ? true : false;

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public boolean delete(T entity, String id)
    {
        try {
            int rs = orm.deleteQuery(entity, id);
            
            return rs == 1 ? true : false;

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

}
