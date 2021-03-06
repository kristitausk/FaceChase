package com.example.facechase_project;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobileComm.mobileComm;

import com.example.facechase_project.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainCameraActivity extends Activity {
	
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;
	public Bitmap killPhoto;

	public MainCameraActivity m;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		m = this; //need this to send the photo
		
	    //doBindService();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_camera);
		this.imageView = (ImageView) this.findViewById(R.id.imageView2);
		Button photoButton = (Button) this.findViewById(R.id.button2);
		photoButton.setOnClickListener(new View.OnClickListener() {




			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
	}




	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (resultCode != RESULT_CANCELED) {
			if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
				this.killPhoto = (Bitmap) data.getExtras().get("data");
				ImageView i=(ImageView) this.findViewById(R.id.imageView2);
				i.setImageBitmap(this.killPhoto);


				
				new sendPictureAsyncTask().execute();
			}
		}
	}
	
    // Called when the user clicks the tagYourKill button
    public void tagYourKill(View v) {
        Intent intent = new Intent(this, TagTargetActivity.class);
        startActivity(intent);
    }




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void goToOptions(View v) {
		Intent intent = new Intent(this, OptionsActivity.class);
		startActivity(intent);
	}
	
	// service stuff from here on
	// also call doBindService() in onCreate()
    private mobileComm mBoundService;
    private boolean mIsBound = false;




    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service. Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            Log.w("Here", "1");
            //Log.w("Test", mBoundService.userID);




            mBoundService = ((mobileComm.LocalBinder) service).getService();




        }




        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;




        }




    };




    void doBindService() {
        // Establish a connection with the service. We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(this, mobileComm.class), mConnection, //
                this.BIND_AUTO_CREATE);
        mIsBound = true;
    }




    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }




    @Override
    protected void onDestroy() {
    	doUnbindService();
        super.onDestroy();
    }
    
    public static int[] rgbToGray(int image1[], int w, int h)
    {
        int[] image2 = new int[w*h];
        for(int i = 0; i < w*h; i++)
        {
            image2[i] = (int)(.299*image1[3*i + 0] + .587*image1[3*i + 1] + .114*image1[3*i + 2]);
        }
        return image2;
    }
    
    public static String writeImageAsString(int data[], int w, int h)
    {
        String s = "" + w + " " + h;
        for(int i = 0; i < w*h; i++)
        {
            s += data[i];
        }
        return s;
    }
   
    public static String bitmapToString(Bitmap b)
    {
        int w = b.getWidth();
        int h = b.getHeight();
        int size = w*h;
        String s = "" + w + " " + h;
        		
        for(int i = 0; i < size; i++)
        {
        	//CHECK LATER
        	
            int r = i/w;
            int c = i%w;
            int pixel = b.getPixel(c,r);
        	
        	s += " " + (int)(.299*Color.red(pixel) + .587*Color.green(pixel) + .114*Color.blue(pixel));    
        }       
        return s;
    }
    
    public static String bitmapToStringOld(Bitmap b)
    {
        int w = b.getWidth();
        int h = b.getHeight();
        int size = w*h;
        int[] array = new int[size*3];
 
        for(int i = 0; i < size; i++)
        {
        	//CHECK LATER
        	
            int r = i/w;
            int c = i%w;
            int pixel = b.getPixel(c,r);
            array[3*i + 0] = Color.red(pixel);
            array[3*i + 1] = Color.green(pixel);
            array[3*i + 2] = Color.blue(pixel);   
            
        }
        int[] gray = rgbToGray(array, w, h);
        
        Log.w("m", writeImageAsString(gray, w, h));
        return writeImageAsString(gray, w, h);
    }
    
    private class sendPictureAsyncTask extends AsyncTask<Void, Void, Void>{
        private List<Double> l;
        
        protected void onPreExecute() {
            super.onPreExecute();
        }




        @Override 
        protected Void doInBackground(Void... params) {




            // needs to be asynchronous task
            String photoString = bitmapToString(m.killPhoto);
            Log.w("message", photoString);
            mBoundService.handleMessageOut("KillRequest" + Constants.stop + photoString);
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            //do nothing?
        }
        
        
    }




}
