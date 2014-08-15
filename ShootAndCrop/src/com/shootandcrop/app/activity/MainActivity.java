package com.shootandcrop.app.activity;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	ImageView picture;
	private final static int PIC_GALLERY = 1;
	private final static int PIC_CROP = 2;
	private Uri picURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initalization();
    }

	private void Initalization() {
		picture = (ImageView) findViewById(R.id.picture);
		
		picture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.picture){
			chooseFromGallery();
		}
	}

	private void chooseFromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
	    startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		if(requestCode == PIC_GALLERY && data != null && data.getData() != null){
			picURI = data.getData();
			//carry out the crop operation
			performCrop();
		}else if(requestCode == PIC_CROP && data != null && data.getData() != null){
			//get the returned data
			Bundle extras = data.getExtras();
			//get the cropped bitmap
			Bitmap thePic = extras.getParcelable("data");
			//display the returned cropped image
			picture.setImageBitmap(thePic);
		}
		super.onActivityResult(requestCode, responseCode, data);
	}

	private void performCrop() {
		//call the standard crop action intent (the user device may not support it)
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		    //indicate image type and Uri
		cropIntent.setDataAndType(picURI, "image/*");
		    //set crop properties
		cropIntent.putExtra("crop", "true");
		    //indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		    //indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		    //retrieve data on return
		cropIntent.putExtra("return-data", true);
		    //start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, PIC_CROP);
	}

}
