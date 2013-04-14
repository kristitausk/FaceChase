package mobileComm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.facechase_project.Constants;
import com.example.facechase_project.MainActivity;

public class mobileComm extends Service {
    private BlockingQueue<String> messagesToServer;
    private String userID = "";
    private List<String> friends;
    private List<String> notifications;

    public String IP = "18.189.81.13";
    /**
     * 
     * May need service permissions in the manifest, not sure how to get them
     * Example
     * <service android:name=".app.MessengerService"
        android:process=":remote" />
        
        from 
        http://developer.android.com/reference/android/app/Service.html
     * 
     * @param a
     * @throws UnknownHostException
     * @throws IOException
     */
    
    
    
    
    @Override
    public void onCreate() {
    	Log.w("onCreate", "onCreate was called");
    	
    	super.onCreate();

        new serverConnection().start();

        
    }
    
/**    public mobileComm(Activity a) throws UnknownHostException, IOException {

        Socket s = new Socket("IP", 4444);
        messagesToServer = new LinkedBlockingQueue<String>();
        InputThread inputThread = new InputThread(s, this);
        OutputThread outputThread = new OutputThread(s, messagesToServer);
        inputThread.start();
        outputThread.start();
        this.a = a;

    } **/
    
   /** public mobileComm()throws UnknownHostException, IOException {
        Socket s = new Socket(IP, 4444);
        messagesToServer = new LinkedBlockingQueue<String>();
        InputThread inputThread = new InputThread(s, this);
        OutputThread outputThread = new OutputThread(s, messagesToServer);
        inputThread.start();
        outputThread.start();
        Log.w("Empty", "Used Empyt Con");
    }**/

    public void handleMessageIn(String msg) {

        // first split the message into key words
        String[] split = msg.split(Constants.itemDelim);

        if (split.length > 0) {
            String s = split[0];
            if (s.equals("User"))
                userID = split[1];
            else if (s.equals("Friends"))
                friends = Arrays.asList(split).subList(1, split.length);
            else if (s.equals("Notification"))
                notifications = Arrays.asList(split).subList(1, split.length);
            else if (s.equals("requestLocation")) {

                // Tried to throw lee's location cod in here, but I

                String locationText = "";
                /**
                 * "location " + Constants.itemDelim+ location.getLatitude() +
                 * Constants.itemDelim + location.getLongitude() +
                 * Constants.itemDelim + (int) location.getAccuracy() ;
                 **/
                messagesToServer.add(locationText);

            }

        }

    }

    // In screens can add code to make them send messages to the server

    public void handleMessageOut(String msg) {
        messagesToServer.add(msg);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new LocalBinder();

    // class for clients to access
    public class LocalBinder extends Binder {
        public mobileComm getService() {
            return mobileComm.this;
        }
    }

    
    //Need to fix for this app.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        
        return START_STICKY;
        
    }
    
    
    public String getUserID() {
        return userID;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getNotifications() {
        return notifications;
    }
    
    
    private mobileComm mc = this;
    private class serverConnection extends Thread {
        public serverConnection(){
            
        }
        
        @Override
        public void run() {
            {

                Socket s;
                try {
                    Log.w("Silly Monster", "before");
                    s = new Socket(IP, 4444);
                    Log.w("Silly Monster", s.toString() + " before");
                    messagesToServer = new LinkedBlockingQueue<String>();
                    InputThread inputThread = new InputThread(s, mc);
                    OutputThread outputThread = new OutputThread(s, messagesToServer);
                    inputThread.start();
                    outputThread.start();
                    
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }



}
