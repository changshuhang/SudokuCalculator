package com.csh.cn.sqlite;

import android.database.sqlite.SQLiteDatabase;

public class MySQLiteOperator {

	private static final String tablename="lishi";
	
	SQLiteDatabase db;
	
	public MySQLiteOperator(SQLiteDatabase db){
		this.db=db;
	}
	
	public void insert(String message,String shijian){
		String sql="insert into "+tablename+" (shijian,message) values (?,?)";
		Object[] args={message,shijian};
		this.db.execSQL(sql, args);
		this.db.close();
	}
	
	public void delete(String shijian){
		String sql="delete from "+tablename+" where shijian = ?";
		Object[] args={shijian};
		this.db.execSQL(sql, args);
		this.db.close();
	}
}
