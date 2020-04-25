import java.io.BufferedReader;
import java.net.Socket;

public class ReceiveHandler extends Thread {
	private Socket socket;
    final BufferedReader in; 
    boolean isActive; 
   
    // constructor 
    public ReceiveHandler(Socket s, BufferedReader in) {
        this.socket = s; 
        this.in = in; 
        this.isActive = true;
    } 
  
    @Override
    public void run() {
        while (isActive) {
            try {
                if (in.ready()) {
                    String response = in.readLine();
                    System.out.println(response);
                    //Thread.sleep(time);
                    
    				// change client-side isActive status to false when /exit
    				if(response.contains("Closing this connection") ) {
    					isActive = false; 
    				}
                }
            } catch (Exception e) {
                System.out.println(e);
            }
           
        } // END while (true)
        
        System.out.println("RecieveHandler closed");
        
    } // END overriden run()
    
} // END class ReceiveHandler



