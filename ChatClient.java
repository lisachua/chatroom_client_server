import java.io.*;
//import java.util.Vector;
import java.net.Socket;


public class ChatClient {
	
	// function main
	public static void main( String[] args ) throws IOException  { 
		// takes two arguments: host and port
		String hostName = args[0];	
		int portNumber = Integer.parseInt(args[1]);	
		
		// initialize socket to null
		Socket socket = null;
		
		
		try
        {
			// establish connection
			socket = new Socket(hostName, portNumber);
			
			// create PrintWriter and BufferedReader objects
			PrintWriter out = new PrintWriter(socket.
						getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader
					(socket.getInputStream()));
			
			BufferedReader userIn = new BufferedReader(new InputStreamReader
					(System.in)); // ... from System.in stream
			
			// threads
			ReceiveHandler receiveThread = new ReceiveHandler(socket, in); 
			receiveThread.start();
			
			SendHandler sendThread = new SendHandler(socket, userIn, out); 
			sendThread.start();
			
			
		
        }
		catch ( IOException e ) {
			//clientSocket.close(); // close clientSocket
			e.printStackTrace();
		}	

		
    } // END function main
         
} // END class ChatClient

