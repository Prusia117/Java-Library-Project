package com.library.kernel.orm;

import java.sql.SQLException;
import java.util.List;

public abstract class Repository<T extends Entity> extends ORM
{
    
    public abstract List<T> getAll() throws SQLException;
/*
    public abstract T getById();

    public abstract T getByName();

    public abstract boolean save(T object);

    public abstract boolean update(T object, int id);

    public abstract boolean delete(int id);*/
}
