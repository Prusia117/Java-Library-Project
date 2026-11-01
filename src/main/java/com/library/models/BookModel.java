package com.library.models;

import java.util.ArrayList;
import com.library.kernel.kernelSql.orm.repository.Repository;
import com.library.entities.BookEntity;
import com.library.kernel.framework.IModel;

public final class BookModel implements IModel<BookEntity>{

	Repository<BookEntity> bookRepositrory = new Repository<BookEntity>(BookEntity.class);

    @Override
	public ArrayList<BookEntity> getAll(){
		ArrayList<BookEntity> entityList = bookRepositrory.getAll();
		return entityList;
	}

	@Override
	public BookEntity getById(String id){
        BookEntity entity = bookRepositrory.getById(id);
		return entity;
	}

    @Override
    public boolean save(BookEntity entity){
        return bookRepositrory.save(entity);
    }

    @Override
	public boolean update(BookEntity entity, String id){
		return bookRepositrory.update(entity, id);
	}

	@Override
	public boolean delete(String id){
		return bookRepositrory.delete(id);
	}
}