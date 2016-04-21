package com.csh.cn.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyCursor {

	private static final String tablename="lishi";
	
	SQLiteDatabase db;
	
	public MyCursor(SQLiteDatabase db){
		this.db=db;
	}
	
	public List<MyData> getData(){
		List<MyData> list=new ArrayList<MyData>();
		String sql="select id,shijian,message from "+tablename;
		Cursor cursor=this.db.rawQuery(sql, null);
		if(cursor!=null){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				MyData myData=new MyData();
				myData.setId(cursor.getInt(0));
				myData.setShijian(cursor.getString(1));
				myData.setMessage(cursor.getString(2));
				list.add(myData);
			}
		}
		return list;
	}
	
	public int getCount(){
		int count=0;
		String sql="select id,shijian,message from "+tablename;
		Cursor cursor=this.db.rawQuery(sql, null);
		if(cursor!=null){
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				count++;
			}
		}
		
		return count;
	}
}
