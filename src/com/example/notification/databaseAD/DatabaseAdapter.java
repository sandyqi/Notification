package com.example.notification.databaseAD;

import java.text.DateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.notification.database.DatabaseHelper;

public class DatabaseAdapter {
	public static final String KEY_TITLE="title";
	public static final String KEY_BODY="body";
	public static final String KEY_ROWID="_id";
	public static final String KEY_CREATED="created";
	private  DatabaseHelper dbHelper;
	private SQLiteDatabase dbSqlite;
	private final Context mCtx;
	private static final String DATABASE_TABLE = "tb_notification";
	
	public DatabaseAdapter(Context context){
		mCtx = context;
	}
	public DatabaseAdapter open() throws SQLException{
		dbHelper = new DatabaseHelper(mCtx);
		dbSqlite = dbHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		dbHelper.close();
	}
	public void addNotif(String title, String body){
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_BODY, body);
		values.put(KEY_CREATED, getCurrentTime());
		dbSqlite.insert(DATABASE_TABLE, null, values);
	}
	public String getCurrentTime(){
		String text = DateFormat.getDateTimeInstance().format(new Date());
		return text;
	}
	public void deleteNotif(long id){
		dbSqlite.delete(DATABASE_TABLE, KEY_ROWID+"="+id, null);
	}
	public Cursor showAllNotif(){
		return dbSqlite.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE,KEY_BODY,KEY_CREATED}, null, null, null, null, null);
	}
	public boolean updateRiji(long id,String title,String body){
		ContentValues values=new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_BODY, body);
		values.put(KEY_CREATED, getCurrentTime());
		
		return dbSqlite.update(DATABASE_TABLE, values, KEY_ROWID+"="+id, null)>0;
	}
}







