package com.library.entities;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;

import java.util.HashMap;

public final class BookEntity implements Entity {

	public String tableName = "T_Books";

	public String primaryKey = "book_id";

	public boolean hasAutoIncrement = true;

    public HashMap<Class<?>, String> relationships = null /*new HashMap<Class<?>, String>() {{}}*/;

    public Integer book_id;
    public String book_title;
    public String book_author;
    public Integer book_year;
    public String book_category;
    public String book_file_path;
    public String book_image_path;

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
	public void setEntityRelationships(ArrayList<Entity> entities)  {this.entityRelationships = entities;}
}

