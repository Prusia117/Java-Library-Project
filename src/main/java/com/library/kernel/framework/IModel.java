package com.library.kernel.framework;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.List;

public abstract interface IModel<T extends Entity> {

    public abstract T getById(String id);

    public abstract boolean update(String id);

    public abstract boolean delete(String id);
}
