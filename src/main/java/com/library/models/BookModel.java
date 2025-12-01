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
	public BookEntity getById(String id){
        BookEntity entity = bookRepositrory.getById(id);
		return entity;
	}

    public boolean save(Integer book_id,String book_title, String book_author,Integer book_year,String book_category,String book_file_path,String book_image_path){
        BookEntity entity = new BookEntity();
        entity.book_id = book_id;
        entity.book_title = book_title;
        entity.book_author = book_author;
        entity.book_year = book_year;
        entity.book_category = book_category;
        entity.book_file_path = book_file_path;
        entity.book_image_path = book_image_path;

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

