package com.dankass.flashcards;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class StudyActivity extends Activity {
	
		private static final int VISIBLE = 0;
		private static final int INVISIBLE = 1;
		private static final int GONE = 2;
	
		private CardsDataSource datasource;
	
		List<Card> flashcards;
		int count = 0;
		Button myButton;
		TextView myTextView;
		ImageView myImageView;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_study);
			
			
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			//ListView myListView = (ListView) findViewById(R.id.myListView);
			myTextView = (TextView) findViewById(R.id.studyView);
			myImageView = (ImageView) findViewById(R.id.studyImage);
			myImageView.setRotation(90);
			myButton = (Button) findViewById(R.id.next);
			
			datasource = new CardsDataSource(this);
			datasource.open();
			
			flashcards = datasource.getAllCards();
			
			if(flashcards.isEmpty()){
				myTextView.setText("Please add Cards to Study");
				myButton.setVisibility(0);
			}else{
				Collections.shuffle(flashcards);
				String front = flashcards.get(count).getFront();
				setActivityBackgroundColor(Color.CYAN);
				
				if(front.contains("file:")){
					//front is an image
					myTextView.setVisibility(GONE);
					myImageView.setVisibility(VISIBLE);
					Uri image = Uri.parse(front);
					myImageView.setImageURI(image);
					myTextView.setText(null);
					
				}else{
					//front is text
					myTextView.setVisibility(VISIBLE); 
					myImageView.setVisibility(GONE);
					myTextView.setText(front);
					myImageView.setImageURI(null);
				}
					
				
				myButton.setText("See Back of Card");
			}
			//myTextView.setText("Yay! you completed the set");
			//myButton.setVisibility(0);
		
		}
		
		public void next(View view){
			if(myButton.getText().equals("See Back of Card")){
				myTextView.setVisibility(VISIBLE); 
				myImageView.setVisibility(GONE);
				myImageView.setImageURI(null);
				myTextView.setText(flashcards.get(count).getBack());
				setActivityBackgroundColor(Color.GREEN);
				myButton.setText("See Next Card");
			}else if(myButton.getText().equals("See Next Card")){
				count++;
				if(count >= flashcards.size()){
					myTextView.setVisibility(VISIBLE); 
					myImageView.setVisibility(GONE);
					myImageView.setImageURI(null);
					myTextView.setText("Yay!");
					myButton.setText("Startover?");
					setActivityBackgroundColor(Color.YELLOW);
					
				}else{
					String front = flashcards.get(count).getFront();
					setActivityBackgroundColor(Color.CYAN);
					if(front.contains("file:")){
						//front is an image
						myTextView.setVisibility(GONE);
						myImageView.setVisibility(VISIBLE);
						myImageView.setImageURI(Uri.parse(front));
						myTextView.setText(null);
						
					}else{
						//front is text
						myTextView.setVisibility(VISIBLE); 
						myImageView.setVisibility(GONE);
						myImageView.setImageURI(null);
						myTextView.setText(front);
					}
						
					myButton.setText("See Back of Card");
				}
			}else if(myButton.getText().equals("Startover?")){
				count = 0;
				Collections.shuffle(flashcards);
				String front = flashcards.get(count).getFront();
				setActivityBackgroundColor(Color.CYAN);
				
				if(front.contains("file:")){
					//front is an image
					myTextView.setVisibility(GONE);
					myImageView.setVisibility(VISIBLE);
					myImageView.setImageURI(Uri.parse(front));
					myTextView.setText(null);
					
				}else{
					//front is text
					myTextView.setVisibility(VISIBLE); 
					myImageView.setVisibility(GONE);
					myImageView.setImageURI(null);
					myTextView.setText(front);
				}
					
				myButton.setText("See Back of Card");
			}
		}
		
		public void setActivityBackgroundColor(int color) {
		    View view = this.getWindow().getDecorView();
		    view.setBackgroundColor(color);
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
