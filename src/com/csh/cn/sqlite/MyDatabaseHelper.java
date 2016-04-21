package com.csh.cn.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper{

	private static final String dataname="sudocalculator";
	private static final int vision=1;
	private static final String tablename="lishi";
	
	int count=0;
	
	public MyDatabaseHelper(Context context,int count) {
		super(context, dataname, null, vision);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(count==0){
			db.execSQL("DROP TABLE IF EXISTS "+tablename);
			db.execSQL("create table "+tablename+"(id int primary key,"
						+"shijian var(30) not null,"
						+"message varchar(81) not null)");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion!=oldVersion){
			this.onCreate(db);
		}
	}

}
