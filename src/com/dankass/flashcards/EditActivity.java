package com.dankass.flashcards;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

public class EditActivity extends Activity {

	private CardsDataSource datasource;
	public ArrayAdapter<Card> aa;
	List<Card> flashcards;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		 View view = this.getWindow().getDecorView();
		 view.setBackgroundColor(Color.LTGRAY);
		
		ListView myListView = (ListView) findViewById(R.id.myListView);
		
		datasource = new CardsDataSource(this);
		datasource.open();
		
		flashcards = datasource.getAllCards();
		
		aa = new ArrayAdapter<Card>(this, android.R.layout.simple_list_item_1, flashcards);
	
		myListView.setAdapter(aa);
		
		//Gets Data from Edit Card to add to DB
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			String value = extras.getString("Card");
			//add new card to data base here split value by `
			
			String cards[] = value.split("`");
			Card card = datasource.createCard(cards[0], cards[1], cards[2]);
			aa.add(card);
			aa.notifyDataSetChanged();
		}
		
		//Processing the clicks on the list
		
	    myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				//need get which card was clicked and then send that information on to the edit card fragment
				//delete the card when pressed so that the edit card can just add it back in.
				String title = flashcards.get(position).getTitle();
				String front = flashcards.get(position).getFront();
				String back = flashcards.get(position).getBack();
				String send = title + "`" + front + "`" + back;
				
				//sending infromation to edit card to populate the fields
				Intent i = new Intent(getApplicationContext(), EditCard.class);
				i.putExtra("oldCard", send);
				startActivity(i);
				
				//removing it from the list
				datasource.deleteCard(flashcards.get(position));
				flashcards.remove(position);
				finish();
			}
	    	
	    });
	    
	    myListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//long press will delete the item
				//TODO if time need to see if there is a prompt
				//toast that the flashcard was deleted.
				String title = flashcards.get(position).getTitle();
				datasource.deleteCard(flashcards.get(position));
				flashcards.remove(position);
				aa.notifyDataSetChanged();
				
				Toast.makeText(getBaseContext(), title + " has been deleted", Toast.LENGTH_LONG).show();
				
				return false;
			}
  	
	    });
	    
	    
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			openAdd();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void openAdd() {
		Intent i = new Intent("com.dankass.flashcards.EditCard");
        startActivity(i);
        finish();
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();		
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
