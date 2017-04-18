package com.dankass.flashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	
	public static final String TABLE_CARDS = "cards";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_FRONT = "front";
	public static final String COLUMN_BACK = "back";
	//public static final String COLUMN_SET = "setOfCards"; //used in future part of app

	private static final String DATABASE_NAME = "cards.db";
	private static final int DATABASE_VERSION = 1;
	
	//Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CARDS + "(" 
			+ COLUMN_ID    + " integer primary key autoincrement, " 
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_FRONT + " text not null, "
			+ COLUMN_BACK  + " text not null"
			+ ");";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
		onCreate(database);

	}

}
