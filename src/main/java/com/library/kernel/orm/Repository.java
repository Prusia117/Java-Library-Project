package com.library.kernel.orm;

import java.util.ArrayList;
import java.util.List;

import com.library.kernel.classes.Colors;

public final class Repository<T extends Entity> implements Colors
{

    private ORM<T> orm;

    private Class<T> entityType;

    public Repository(Class<T> type) throws ORMException
    {
        orm = new ORM<T>();

        this.entityType = type;
    }
    
    public List<T> getAll()
    {
        try {

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),null);
            
            return entityArrayList;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return null;
        }
    }
    
    public List<T> getById(String id) throws ORMException
    {
        try {
            
            if(id == null){
                throw new ORMException("Invalid values provided");
            }

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),id);
            
            return entityArrayList;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return null;
        }
    }
    
    public boolean save(T entity) throws ORMException
    {
        try {

            if(entity == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, null);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }
    
    public boolean update(T entity, String id) throws ORMException
    {
        try {

            if(id == null || entity == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, id);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }

    public boolean delete(String id) throws ORMException
    {
        try {

            if(id == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.deleteQuery(EntityFactory.createEntity(entityType), id);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }

}
