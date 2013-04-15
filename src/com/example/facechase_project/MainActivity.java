package com.example.facechase_project;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.example.facechase_project.R;

import mobileComm.mobileComm;
import android.os.AsyncTask;
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
import android.widget.TextView;

public class MainActivity extends Activity {

    // public Activity a = this;
    // public mobileComm server;
    private Intent srv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//do
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        srv = new Intent(this, mobileComm.class);
        startService(srv);
        doBindService();
            Log.w("Here", "Success to Start service.");
        try {
            
            
        } catch (Exception e) {
            // service could not be started
            Log.w("FAIL", "Failed to Start service.");
        }
        super.onCreate(savedInstanceState);
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called when the user clicks the sendLogin button
    public void sendLogin(View view) {
        EditText editTextEmail = (EditText) findViewById(R.id.email_for_login);
        EditText editTextPassword = (EditText) findViewById(R.id.password_for_login);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        while (mBoundService == null) {}

        mBoundService.handleMessageOut("Login" + " | " + email + " | "
                + password);
        
        while (mBoundService.getUserID().equals("Waiting")) {}
        
        if (mBoundService.getUserID().equals("NotValid") {
        	//need popup that says invalid
        	startActivity(new Intent(this, MainActivity.class));
        }
        
        else startActivity(newIntent(this, MainCameraActivity.class));
    }

    // Called when the user clicks the goToRegister button
    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    

    // service stuff
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
