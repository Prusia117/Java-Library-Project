package com.library.kernel.orm;

import java.util.ArrayList;
import java.util.List;

import com.library.kernel.classes.Colors;

public final class Repository<T extends Entity> implements Colors
{

    private ORM<T> orm;

    private Class<T> type;

    public Repository(Class<T> type){
        orm = new ORM<T>();

        this.type = type;
    }
    
    public List<T> getAll()
    {
        try {

            T ca = EntityFactory.createEntity(type);

            ArrayList<T> rs = orm.selectQuery(EntityFactory.createEntity(type),null);
            
            return rs;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(RED + e + RESET);
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

    public boolean delete(String id)
    {
        try {
            int rs = orm.deleteQuery(EntityFactory.createEntity(type), id);
            
            return rs == 1 ? true : false;

        } catch (Exception e) {
            // TODO: Logger implementation
            System.out.println(RED + "SQL Error Query Execution:" + e + RESET);
            return false;
        }
    }

}
