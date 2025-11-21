package com.library.entities;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;

import java.util.HashMap;

public final class BooksEntity implements Entity {

	private String tableName = "T_Books";

	private String primaryKey = "book_id";

	private boolean hasAutoIncrement = true;

	private HashMap<Class<?>, String> relationships = null /*new HashMap<Class<?>, String>() {{}}*/;

    Integer book_id;
    String book_name;
    String book_author;
    Integer book_year;
    String book_category;
    String book_file_path;
    String book_image_path;

	public ArrayList<Entity> entityRelationships;

	@Override
	public String getPrimaryKey() {
		return primaryKey;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public boolean getAutoIncrement() {
		return hasAutoIncrement;
	}

	@Override
	public HashMap<Class<?>, String> getRelationships(){
		return relationships;
	}

	@Override
	public void setEntityRelationships(ArrayList<Entity> entities){
		this.entityRelationships = entities;
	}
}

