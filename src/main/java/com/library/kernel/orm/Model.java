package com.library.kernel.orm;

import java.util.List;

public abstract class Model<T>
{
    
    public abstract List<T> getAll();
/* 
    public abstract T getById();

    public abstract T getByName();

    public abstract boolean save(T object);

    public abstract boolean update(T object, int id);

    public abstract boolean delete(int id);*/
}
