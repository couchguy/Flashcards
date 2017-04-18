package com.dankass.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void EditActivity(View view) {
		Intent i = new Intent("com.dankass.flashcards.EditActivity");
        startActivity(i);
	}
	
	public void StudyActivity(View view) {
		Intent i = new Intent("com.dankass.flashcards.StudyActivity");
        startActivity(i);
	}

}
