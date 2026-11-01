package com.library.models;

import java.util.ArrayList;

import com.library.kernel.kernelSql.orm.repository.Repository;

import com.library.entities.UserEntity;

import com.library.kernel.framework.IModel;
import com.library.kernel.framework.auth.PasswordHasher;

public final class UserModel implements IModel<UserEntity>{

    Repository<UserEntity> userRepositrory = new Repository<UserEntity>(UserEntity.class);

    @Override
    public ArrayList<UserEntity> getAll(){
        ArrayList<UserEntity> entityList = userRepositrory.getAll();
        return entityList;
    }

    @Override
    public UserEntity getById(String id){
        UserEntity entity = userRepositrory.getById(id);
        return entity;
    }

    @Override
    public boolean save(UserEntity entity){
        return userRepositrory.save(entity);
    }

    @Override
    public boolean update(UserEntity entity, String id){
        return userRepositrory.update(entity, id);
    }

    @Override
    public boolean delete(String id){
        return userRepositrory.delete(id);
    }

    public boolean login(String userEmail/*Correo*/, String userPassword/*Contraseña*/) {

        ArrayList<UserEntity> users = userRepositrory.getByField("user_mail",userEmail);

        if (users.size() == 0) {
            return false;
        }

        UserEntity user = users.get(0);
        return PasswordHasher.verify(userPassword, user.user_password);
    }
}

