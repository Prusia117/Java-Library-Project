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

    public boolean save(Integer id, String name, String mail, String password){
        UserEntity entity = new UserEntity();
        entity.user_id = id;
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

}

