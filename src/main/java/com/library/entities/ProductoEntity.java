package com.library.entities;

import com.library.kernel.orm.Entity;

import java.util.ArrayList;

import java.util.HashMap;

public final class ProductoEntity implements Entity {

	private String tableName = "";
	private String primaryKey = "";
	private boolean hasAutoIncrement = true; 
	private HashMap<Class<?>, String> relationships = new HashMap<Class<?>, String>() {{}};


	public ArrayList<Entity> entityRelationships;

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean getAutoIncrement() {
		return hasAutoIncrement;
	}

	public HashMap<Class<?>, String> getRelationships(){
		return relationships;
	}

	public void setEntityRelationships(ArrayList<Entity> entities){
		this.entityRelationships = entities;
	}
}

