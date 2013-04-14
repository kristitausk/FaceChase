package mobileComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;



public class InputThread extends Thread{
    Socket s;
    mobileComm m;
    public InputThread(Socket s, mobileComm m){
        this.s = s;
        this.m = m;
    }
    
    
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String input;
            while(true) {
                input = in.readLine();
                //not sure if I'll need an equivalent
                //input = input.replaceAll(EOFRE,"\n");
                handleIncomingMessage(input);
            }            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
    
    /**
     * Passes off incoming messages to the controller to be redistributed and acted upon
     * @param input incoming message string
     */
    public void handleIncomingMessage(final String input) {
        m.handleMessageIn(input);
        
        
    }
}

