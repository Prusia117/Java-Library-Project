package com.library.models;

import java.util.ArrayList;

import com.library.kernel.kernelSql.orm.repository.Repository;

import com.library.entities.UserEntity;

import com.library.kernel.framework.IModel;

public final class UserModel implements IModel{

    Repository<UserEntity> userRepositrory = new Repository<UserEntity>(UserEntity.class);

    public ArrayList<UserEntity> getAll(){
        ArrayList<UserEntity> entityList = userRepositrory.getAll();
        return entityList;
    }

    @Override
    public UserEntity getById(String id){
        UserEntity entity = userRepositrory.getById(id);
        return entity;
    }

    public boolean save(String name, String mail, String password){
        UserEntity entity = new UserEntity();

        entity.user_name = name;
        entity.user_mail = mail;
        entity.user_password = password;

        return userRepositrory.save(entity);
    }

    @Override
    public boolean update(String id){
        UserEntity entity = new UserEntity();
        return userRepositrory.update(entity, id);
    }

    @Override
    public boolean delete(String id){
        return userRepositrory.delete(id);
    }

    public boolean login(String user_mail/*Correo*/, String user_password/*Contraseña*/) {

        ArrayList<UserEntity> users = userRepositrory.getByField("user_mail",user_mail);

        if (users.size() == 0) {
            return false;
        }

        UserEntity user = users.get(0);
        if (user.user_password.equals(user_password)) {
            return true;
        }

        return false;
    }
}