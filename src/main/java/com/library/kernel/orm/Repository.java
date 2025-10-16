package com.library.kernel.orm;

import java.sql.SQLException;
import java.util.List;

public final class Repository<T extends Entity> 
{

    private ORM orm;

    Repository(){
        orm = new ORM();
    }
    
    public /*List<T>*/ void getAll()
    {
        orm.selectQuery(null, null);
    }
/*
    public abstract T getById();

    public abstract T getByName();

    public abstract boolean save(T object);

    public abstract boolean update(T object, int id);

    public abstract boolean delete(int id);*/
}
