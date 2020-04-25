import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class SendHandler extends Thread {
	private Socket socket;
	final BufferedReader userIn;
    final PrintWriter out; 
    boolean isActive; 
    
    // constructor 
    public SendHandler(Socket s, BufferedReader userIn, PrintWriter out) {
        this.socket = s; 
        this.userIn= userIn;
        this.out = out; 
        this.isActive = true; 
    } 
  
    @Override
    public void run() { 
    	while (isActive) {
    		try {
				// read in client input from console
				String userInput= userIn.readLine();
				// write client input out to socket
				out.println(userInput);
				
				// change client-side isActive status to false when /exit
				if(userInput.contains("/quit") ) {
					isActive = false; 
				}
			   
    		}
    		catch ( IOException e ) {
			e.printStackTrace();
    		} // END catch block
    	} // END while(true)
    	
        System.out.println("SendHandler closed");
        
        
    } // END overriden run()
    
} // END class SendHandler

