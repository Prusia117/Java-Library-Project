package com.library.models;

import java.util.ArrayList;

import com.library.kernel.kernelSql.orm.repository.Repository;

import com.library.entities.BookEntity;

import com.library.kernel.framework.IModel;

public final class BookModel implements IModel{

	Repository<BookEntity> bookRepositrory = new Repository<BookEntity>(BookEntity.class);

	public ArrayList<BookEntity> getAll(){
		ArrayList<BookEntity> entityList = bookRepositrory.getAll();
		return entityList;
	}

	@Override
	public ArrayList<BookEntity> getById(String id){
		ArrayList<BookEntity> entityList = bookRepositrory.getById(id);
		return entityList;
	}

	@Override
	public boolean save(){
		BookEntity entity = new BookEntity();
		return bookRepositrory.save(entity);
	}

	@Override
	public boolean update(String id){
		BookEntity entity = new BookEntity();
		return bookRepositrory.update(entity, id);
	}

	@Override
	public boolean delete(String id){
		return bookRepositrory.delete(id);
	}

}

