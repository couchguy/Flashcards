package com.dankass.flashcards;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CardsDataSource {
	
	//Database Fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {
			MySQLiteHelper.COLUMN_ID, 
			MySQLiteHelper.COLUMN_TITLE,
			MySQLiteHelper.COLUMN_FRONT,
			MySQLiteHelper.COLUMN_BACK};
			
	public CardsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Card createCard(String title, String front, String back) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		values.put(MySQLiteHelper.COLUMN_FRONT, front);
		values.put(MySQLiteHelper.COLUMN_BACK, back);
		
		
		long insertID = database.insert(MySQLiteHelper.TABLE_CARDS,  null,  values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CARDS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
		cursor.moveToFirst();
		Card newCard = cursorToCard(cursor);
		return newCard;
	}
	
	public void deleteCard(Card card){
		long id = card.getId();
		database.delete(MySQLiteHelper.TABLE_CARDS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Card> getAllCards() {
		List<Card> cards = new ArrayList<Card>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CARDS, allColumns, null, null, null, null, MySQLiteHelper.COLUMN_TITLE);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Card card = cursorToCard(cursor);
			cards.add(card);
			cursor.moveToNext();
		}
		//make sure to close the cursor
		cursor.close();
		return cards;
	}
	
	private Card cursorToCard(Cursor cursor) {
		Card card = new Card();
		card.setId(cursor.getLong(0));
		card.setTitle(cursor.getString(1));
		card.setFront(cursor.getString(2));
		card.setBack(cursor.getString(3));
		return card;
	}

}
