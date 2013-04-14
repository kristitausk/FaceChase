package com.example.facechase_project;

import com.example.facechase_project.R;
import mobileComm.mobileComm;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class RegistrationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		doBindService();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	public void continueRegistration(View view) {
		Intent intent = new Intent(this, RegistrationPictureInstructionActivity.class);
		EditText editTextName = (EditText) findViewById(R.id.register_name_message);
		EditText editTextEmail = (EditText) findViewById(R.id.register_email_message);
		EditText editTextPassword = (EditText) findViewById(R.id.register_password_message);
		EditText editTextPassword2 = (EditText) findViewById(R.id.register_password2_message);
		String name = editTextName.getText().toString();
		String email = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();
		String password2 = editTextPassword2.getText().toString();
		
		
        if(mIsBound && password.equals(password2))
                mBoundService.handleMessageOut("AddUser" + Constants.stop + name+Constants.stop + email+Constants.stop + password);
        
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
        super.onDestroy();
        doUnbindService();
    }
	
	

	

}

