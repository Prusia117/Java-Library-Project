package com.library.kernel.framework;

import com.library.kernel.orm.entity.Entity;

import java.util.List;

public abstract interface Model {

    public abstract List<Entity> getById(String id);

    public abstract boolean save();

    public abstract boolean update(String id);

    public abstract boolean delete(String id);
}
