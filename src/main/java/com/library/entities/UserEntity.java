package com.library.entities;

import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;

import java.util.HashMap;

public final class UserEntity implements Entity {

	public String tableName = "T_Users";

	public String primaryKey = "user_id";

	public boolean hasAutoIncrement = true;

	public HashMap<Class<?>, String> relationships = null /*new HashMap<Class<?>, String>() {{}}*/;

    public Integer user_id;
    public String user_name;
    public String user_mail;
    public String user_password;

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

