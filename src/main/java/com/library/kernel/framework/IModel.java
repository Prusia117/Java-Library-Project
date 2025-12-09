package com.library.kernel.framework;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract interface IModel<T extends Entity> {

    public abstract ArrayList<T> getAll();

    public abstract T getById(String id);

    public abstract boolean save(T entity);

    public abstract boolean update(T entity, String id);

    public abstract boolean delete(String id);
}
