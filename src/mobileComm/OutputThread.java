package mobileComm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/* 
 * See GUIMain.java for the concurrency argument for threads/objects on the client 
 */

/**
 * The ClientOutputThread handles the client side output to the server. Every message
 * that the clients send to the server passes through these methods. It maintains a queue
 * of messages to be sent to the server, which is polled in the main runtime loop.
 * Blocks while waiting for new messages.
 * 
 * 
 */
public class OutputThread extends Thread {
    Socket sock;
    private final BlockingQueue<String> messagesToServer;

    /**
     * The constructor takes a reference to the socket and to its message queue.
     * @param sock
     * @param messagesToServer the message queue that is contained in the Controller
     */
    public OutputThread(Socket sock, BlockingQueue<String> messagesToServer) {
        
        this.sock = sock;
        this.messagesToServer = messagesToServer;
    }
    
    /**
     * The run method constantly calls 'take()' on the message queue, which blocks while 
     * the queue is empty, so as to not cause errors.
     * Also, replaces newline characters with our constant "obscure" character
     * which should never show up in an actual transmission, or everything would break
     * 
     */
    public void run() {
        try {
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            String msgToServer = "";
            while(true) {
                msgToServer = messagesToServer.take();
                out.println(msgToServer);     
            }            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }
}