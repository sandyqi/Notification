package com.example.notification.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "db_bi";
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_DATABASE="create table tb_notification (_id integer primary key autoincrement, "
			+ "title text not null, body text not null, created text not null);";
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATABASE);
	} // just for one time. Or if there is not a table called tb_notification, onCreate will be called

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS notification");
		onCreate(db);
	}

}
