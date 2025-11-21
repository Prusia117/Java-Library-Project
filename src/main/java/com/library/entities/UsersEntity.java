package com.library.entities;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;

import java.util.HashMap;

public final class UsersEntity implements Entity {

	private String tableName = "T_Users";

	private String primaryKey = "user_id";

	private boolean hasAutoIncrement = true; 

	private HashMap<Class<?>, String> relationships = null /*new HashMap<Class<?>, String>() {{}}*/;

    Integer user_id;
    String user_name;
    String user_mail;
    String user_password;

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

