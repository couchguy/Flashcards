package com.dankass.flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class EditCard extends Activity {
	
	EditText myTitle;
	EditText myFront;
	EditText myBack;
	//Button pictureButton;
	//ImageView image;
	boolean editing = false;
	
	private Camera mCamera;
    private CameraPreview mPreview;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_card);
		
		 View view = this.getWindow().getDecorView();
		 view.setBackgroundColor(Color.LTGRAY);
		
		 myTitle = (EditText) findViewById(R.id.title);
		 myFront = (EditText) findViewById(R.id.front);
		 myBack  = (EditText) findViewById(R.id.back);
		 
		// Create an instance of Camera
	    mCamera = getCameraInstance();

	    // Create our Preview view and set it as the content of our activity.
	    mPreview = new CameraPreview(this, mCamera);
	    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	    preview.addView(mPreview);
	    
	    // Add a listener to the Capture button
	    Button captureButton = (Button) findViewById(R.id.button_capture);
	    captureButton.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // get an image from the camera
	                mCamera.takePicture(null, null, mPicture);
	            }
	        }
	    );
		
		 
		 Bundle extras = getIntent().getExtras();
			if(extras != null){
				String value = extras.getString("oldCard");
				//add new card to data base here split value by `
				
				String cards[] = value.split("`");
				String title = cards[0];
				String front = cards[1];
				String back = cards[2];
				
				myTitle.setText(title);
				myFront.setText(front);
				myBack.setText(back);
				editing = true;
			}
		 
		 
	}
	
	public void save(View view) {
		String title = myTitle.getText().toString();
		String front = myFront.getText().toString();
		String back  =  myBack.getText().toString();
		String send = title + "`" + front + "`" + back;
		
		//Error checking checking for blank or null
		if(title.equals(null) || title.equals("")){
			Toast.makeText(getBaseContext(), "Title cannot be blank", Toast.LENGTH_LONG).show();
		} else if(front.equals(null) || front.equals("")){
			Toast.makeText(getBaseContext(), "Front cannot be blank", Toast.LENGTH_LONG).show();
		} else if(back.equals(null) || back.equals("")){
			Toast.makeText(getBaseContext(), "Back cannot be blank", Toast.LENGTH_LONG).show();
		}else {
			
			Intent i = new Intent(getApplicationContext(), EditActivity.class);
			i.putExtra("Card", send);
			startActivity(i);
		
			Toast.makeText(getBaseContext(), title + " has been added!", Toast.LENGTH_LONG).show();
		
			finish();
		}
	}
	
	public void cancel(View view) {
		if(editing == true){
			//this means that the user is editing but hit cancel, but we already deleted it from database
			//so we need to add it back in. 
			String title = myTitle.getText().toString();
			String front = myFront.getText().toString();
			String back  =  myBack.getText().toString();
			String send = title + "`" + front + "`" + back;
			
			Intent i = new Intent(getApplicationContext(), EditActivity.class);
			i.putExtra("Card", send);
			startActivity(i);
			//this should work. only need if the user cancels after clicking to edit it.
		}else{
			
			Intent i = new Intent("com.dankass.flashcards.EditActivity");
			startActivity(i);
		}
		finish();
	}
	
	@Override
	public void onBackPressed() {
	    cancel(findViewById(R.layout.activity_edit_card));
	    return;
	}
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {

	        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	        if (pictureFile == null){
	            Log.d("TAG", "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d("TAG", "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d("TAG", "Error accessing file: " + e.getMessage());
	        }
	        
	        String uriString = getOutputMediaFileUri(MEDIA_TYPE_IMAGE).toString();
	        myFront.setText(uriString);
	        Toast.makeText(getBaseContext(), "Picture Taken!", Toast.LENGTH_LONG).show();
			
	       // myFront.setVisibility(1);
	    }
	};
	
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	        c.setDisplayOrientation(90);
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    
	    return c; // returns null if camera is unavailable
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
	}
		
}
