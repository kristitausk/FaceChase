package com.example.facechase_project;

import java.util.ArrayList;
import java.util.List;

import mobileComm.mobileComm;

import com.example.facechase_project.R;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TagTargetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//doBindService();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_target);
		RadioGroup rg = (RadioGroup) findViewById(R.id.targetButtons);
		
		//List<String> targets = mBoundService.getFriends();
		List<String> targets = new ArrayList<String>();
		targets.add("Friend 1");
		targets.add("Friend 2");
		for (int i=0; i<targets.size(); i++) {
			RadioButton rb = new RadioButton(this);
			rb.setText(targets.get(i));
			LinearLayout.LayoutParams lp = new RadioGroup.LayoutParams(
	                RadioGroup.LayoutParams.MATCH_PARENT,
	                RadioGroup.LayoutParams.WRAP_CONTENT);
			rg.addView(rb, 0, lp);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_target, menu);
		return true;
	}

	
	public void submit(View v) {
		RadioGroup rg = (RadioGroup) findViewById(R.id.targetButtons);
		int id = rg.getCheckedRadioButtonId();
		RadioButton rb = (RadioButton) findViewById(id);
		String target = rb.getText().toString();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Your kill has been submitted!");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Your notifications will be updated with this kill request shortly.")
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						next();
					}
				  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			
		//if(mIsBound)
            //mBoundService.handleMessageOut("TagTarget" + Constants.stop + target);
	}
	
	public void next() {
		Intent intent = new Intent(this, MainCameraActivity.class);
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

}
