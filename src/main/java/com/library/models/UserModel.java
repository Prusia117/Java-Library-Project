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
	public ArrayList<UserEntity> getById(String id){
		ArrayList<UserEntity> entityList = userRepositrory.getById(id);
		return entityList;
	}

	public boolean save(){
		UserEntity entity = new UserEntity();
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

